package com.automotiva.estetica.rick.consumidor_ordem_servico.service;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoResponse;
import com.automotiva.estetica.rick.consumidor_ordem_servico.repository.CalendarioEventoRepository;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CalendarioEventoPersistenceService - Testes Unitários")
class CalendarioEventoPersistenceServiceTest {

    @Mock
    private CalendarioEventoRepository repository;

    @InjectMocks
    private CalendarioEventoPersistenceService persistenceService;

    private CalendarioEventoResponse response;
    private OrdemServicoCriadaEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Dados de teste
        response = CalendarioEventoResponse.builder()
                .id("google_event_123")
                .titulo("Atendimento veículo - ABC-1234")
                .descricao("ID Ordem: 456\nPlaca do Veículo: ABC-1234")
                .localizacao("Estética Automotiva Rick")
                .dataHoraInicio(Instant.now())
                .dataHoraFim(Instant.now().plusSeconds(3600))
                .build();

        event = new OrdemServicoCriadaEvent(
                456L,
                "ABC-1234",
                LocalDateTime.now().plusDays(1),
                List.of("Lavagem", "Cera"),
                "Cliente VIP"
        );
    }

    @Test
    @DisplayName("Deve persistir evento com sucesso quando não há duplicata")
    void testPersistirEvento_Success() {
        // Arrange
        when(repository.existsByEventoIdGoogle("google_event_123")).thenReturn(false);

        CalendarioEvento calendarioEvento = CalendarioEvento.builder()
                .id(1L)
                .idOrdemServico(456L)
                .eventoIdGoogle("google_event_123")
                .titulo("Atendimento veículo - ABC-1234")
                .status("ATIVO")
                .build();

        when(repository.save(any(CalendarioEvento.class))).thenReturn(calendarioEvento);

        // Act
        CalendarioEvento resultado = persistenceService.persistirEvento(response, event);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(456L, resultado.getIdOrdemServico());
        assertEquals("google_event_123", resultado.getEventoIdGoogle());
        assertEquals("ATIVO", resultado.getStatus());

        // Verify
        verify(repository, times(1)).existsByEventoIdGoogle("google_event_123");
        verify(repository, times(1)).save(any(CalendarioEvento.class));
    }

    @Test
    @DisplayName("Deve retornar evento existente quando há duplicata")
    void testPersistirEvento_Duplicata() {
        // Arrange
        CalendarioEvento eventoExistente = CalendarioEvento.builder()
                .id(99L)
                .idOrdemServico(456L)
                .eventoIdGoogle("google_event_123")
                .titulo("Atendimento veículo - ABC-1234")
                .status("ATIVO")
                .build();

        when(repository.existsByEventoIdGoogle("google_event_123")).thenReturn(true);
        when(repository.findByEventoIdGoogle("google_event_123")).thenReturn(Optional.of(eventoExistente));

        // Act
        CalendarioEvento resultado = persistenceService.persistirEvento(response, event);

        // Assert
        assertNotNull(resultado);
        assertEquals(99L, resultado.getId());

        // Verify - não deve salvar novamente
        verify(repository, times(0)).save(any(CalendarioEvento.class));
    }

    @Test
    @DisplayName("Deve encontrar eventos por ID da ordem de serviço")
    void testEncontrarEventosPorOrdem() {
        // Arrange
        Long idOrdemServico = 456L;
        CalendarioEvento evento1 = CalendarioEvento.builder()
                .id(1L)
                .idOrdemServico(idOrdemServico)
                .build();
        CalendarioEvento evento2 = CalendarioEvento.builder()
                .id(2L)
                .idOrdemServico(idOrdemServico)
                .build();

        when(repository.findByIdOrdemServico(idOrdemServico))
                .thenReturn(List.of(evento1, evento2));

        // Act
        List<CalendarioEvento> resultado = persistenceService.encontrarEventosPorOrdem(idOrdemServico);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());

        // Verify
        verify(repository, times(1)).findByIdOrdemServico(idOrdemServico);
    }

    @Test
    @DisplayName("Deve encontrar evento pelo ID do Google Calendar")
    void testEncontrarEventoPorIdGoogle() {
        // Arrange
        String eventoIdGoogle = "google_event_123";
        CalendarioEvento evento = CalendarioEvento.builder()
                .id(1L)
                .eventoIdGoogle(eventoIdGoogle)
                .build();

        when(repository.findByEventoIdGoogle(eventoIdGoogle))
                .thenReturn(Optional.of(evento));

        // Act
        Optional<CalendarioEvento> resultado = persistenceService.encontrarEventoPorIdGoogle(eventoIdGoogle);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals(eventoIdGoogle, resultado.get().getEventoIdGoogle());

        // Verify
        verify(repository, times(1)).findByEventoIdGoogle(eventoIdGoogle);
    }

    @Test
    @DisplayName("Deve atualizar status do evento")
    void testAtualizarStatus() {
        // Arrange
        CalendarioEvento evento = CalendarioEvento.builder()
                .id(1L)
                .status("ATIVO")
                .build();

        CalendarioEvento eventoAtualizado = CalendarioEvento.builder()
                .id(1L)
                .status("CANCELADO")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(evento));
        when(repository.save(any(CalendarioEvento.class))).thenReturn(eventoAtualizado);

        // Act
        CalendarioEvento resultado = persistenceService.atualizarStatus(1L, "CANCELADO");

        // Assert
        assertNotNull(resultado);
        assertEquals("CANCELADO", resultado.getStatus());

        // Verify
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(CalendarioEvento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar evento inexistente")
    void testAtualizarStatus_NotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            persistenceService.atualizarStatus(999L, "CANCELADO");
        });

        // Verify
        verify(repository, times(0)).save(any(CalendarioEvento.class));
    }

    @Test
    @DisplayName("Deve contar eventos de uma ordem de serviço")
    void testContarEventosPorOrdem() {
        // Arrange
        Long idOrdemServico = 456L;
        when(repository.countByIdOrdemServico(idOrdemServico)).thenReturn(3L);

        // Act
        Long resultado = persistenceService.contarEventosPorOrdem(idOrdemServico);

        // Assert
        assertEquals(3L, resultado);

        // Verify
        verify(repository, times(1)).countByIdOrdemServico(idOrdemServico);
    }

    @Test
    @DisplayName("Deve converter Instant para LocalDateTime com timezone correto")
    void testConversaoInstantParaLocalDateTime() {
        // Arrange
        Instant instant = Instant.parse("2026-03-20T14:00:00Z");
        String fusoSaoPaulo = "America/Sao_Paulo";

        // Act
        LocalDateTime resultado = LocalDateTime.ofInstant(instant, ZoneId.of(fusoSaoPaulo));

        // Assert
        assertNotNull(resultado);
        // São Paulo está UTC-3, então 14:00 UTC = 11:00 São Paulo
        assertEquals(11, resultado.getHour());
    }
}

