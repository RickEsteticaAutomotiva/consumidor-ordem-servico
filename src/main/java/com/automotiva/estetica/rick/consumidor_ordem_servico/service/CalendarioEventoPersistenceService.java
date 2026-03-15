package com.automotiva.estetica.rick.consumidor_ordem_servico.service;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoResponse;
import com.automotiva.estetica.rick.consumidor_ordem_servico.repository.CalendarioEventoRepository;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarioEventoPersistenceService {

    private final CalendarioEventoRepository repository;

    private static final String FUSO_SAO_PAULO = "America/Sao_Paulo";

    /**
     * Persiste o evento criado no Google Calendar no banco de dados
     *
     * @param response Resposta do Google Calendar com os dados do evento criado
     * @param event Evento original da ordem de serviço
     * @return CalendarioEvento persistido
     */
    @Transactional
    public CalendarioEvento persistirEvento(CalendarioEventoResponse response, OrdemServicoCriadaEvent event) {
        try {
            log.info("Iniciando persistência do evento no banco de dados");

            // Verificar se evento já existe (evitar duplicatas)
            if (repository.existsByEventoIdGoogle(response.getId())) {
                log.warn("Evento com ID Google {} já existe no banco de dados", response.getId());
                return repository.findByEventoIdGoogle(response.getId()).orElse(null);
            }

            // Converter Instant para LocalDateTime
            LocalDateTime dataInicio = LocalDateTime.ofInstant(response.getDataHoraInicio(), ZoneId.of(FUSO_SAO_PAULO));
            LocalDateTime dataFim = LocalDateTime.ofInstant(response.getDataHoraFim(), ZoneId.of(FUSO_SAO_PAULO));

            // Criar entidade CalendarioEvento
            CalendarioEvento calendarioEvento = CalendarioEvento.builder()
                    .idOrdemServico(event.IdOrdemServico())
                    .eventoIdGoogle(response.getId())
                    .titulo(response.getTitulo())
                    .descricao(response.getDescricao())
                    .localizacao(response.getLocalizacao())
                    .dataHoraInicio(dataInicio)
                    .dataHoraFim(dataFim)
                    .fusoHorario(FUSO_SAO_PAULO)
                    .placaVeiculo(event.placaVeiculo())
                    .build();

            // Persistir no banco
            CalendarioEvento saved = repository.save(calendarioEvento);

            log.info("✅ Evento persistido com sucesso. ID do banco: {}, ID Google: {}, ID Ordem: {}",
                    saved.getId(), saved.getEventoIdGoogle(), saved.getIdOrdemServico());

            return saved;

        } catch (Exception e) {
            log.error("❌ Erro ao persistir evento no banco de dados", e);
            throw new RuntimeException("Falha ao persistir evento: " + e.getMessage(), e);
        }
    }

    /**
     * Encontra todos os eventos associados a uma ordem de serviço
     */
    @Transactional(readOnly = true)
    public java.util.List<CalendarioEvento> encontrarEventosPorOrdem(Long idOrdemServico) {
        log.debug("Buscando eventos para ordem de serviço ID: {}", idOrdemServico);
        return repository.findByIdOrdemServico(idOrdemServico);
    }

    /**
     * Encontra um evento pelo ID do Google Calendar
     */
    @Transactional(readOnly = true)
    public java.util.Optional<CalendarioEvento> encontrarEventoPorIdGoogle(String eventoIdGoogle) {
        log.debug("Buscando evento por ID Google: {}", eventoIdGoogle);
        return repository.findByEventoIdGoogle(eventoIdGoogle);
    }

    /**
     * Atualiza o status de um evento
     */
    @Transactional
    public CalendarioEvento atualizarStatus(Long id, String novoStatus) {
        try {
            CalendarioEvento evento = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado: " + id));

            CalendarioEvento updated = repository.save(evento);

            log.info("Status do evento ID {} atualizado para: {}", id, novoStatus);
            return updated;
        } catch (Exception e) {
            log.error("Erro ao atualizar status do evento ID {}", id, e);
            throw e;
        }
    }

    /**
     * Conta quantos eventos estão associados a uma ordem de serviço
     */
    @Transactional(readOnly = true)
    public Long contarEventosPorOrdem(Long idOrdemServico) {
        return repository.countByIdOrdemServico(idOrdemServico);
    }
}

