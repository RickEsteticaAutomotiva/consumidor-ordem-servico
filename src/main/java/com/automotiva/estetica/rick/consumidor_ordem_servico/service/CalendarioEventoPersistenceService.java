package com.automotiva.estetica.rick.consumidor_ordem_servico.service;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoResponse;
import com.automotiva.estetica.rick.consumidor_ordem_servico.repository.CalendarioEventoRepository;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            log.info("Persistindo evento Google {} para ordem {}", response.getId(), event.IdOrdemServico());

            if (repository.existsByEventoIdGoogle(response.getId())) {
                return repository.findByEventoIdGoogle(response.getId()).orElse(null);
            }

            LocalDateTime dataInicio = LocalDateTime.ofInstant(response.getDataHoraInicio(), ZoneId.of(FUSO_SAO_PAULO));
            LocalDateTime dataFim = LocalDateTime.ofInstant(response.getDataHoraFim(), ZoneId.of(FUSO_SAO_PAULO));

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

            return repository.save(calendarioEvento);

        } catch (Exception e) {
            log.error("Erro ao persistir evento {}", response.getId(), e);
            throw new RuntimeException("Falha ao persistir evento: " + e.getMessage(), e);
        }
    }
}
