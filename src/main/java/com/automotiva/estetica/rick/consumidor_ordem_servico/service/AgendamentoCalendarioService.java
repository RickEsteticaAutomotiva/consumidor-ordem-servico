package com.automotiva.estetica.rick.consumidor_ordem_servico.service;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoAtualizacaoRequest;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioEventoRequest;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.CalendarioPort;
import com.automotiva.estetica.rick.dto.OrdemServicoAtualizadaEvent;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoCalendarioService {

    private final CalendarioPort calendarioPort;
    private final CalendarioEventoPersistenceService persistenceService;
    private final OrdemServicoStatusService ordemServicoStatusService;

    private static final String FUSO_SAO_PAULO = "America/Sao_Paulo";
    private static final String LOCALIZACAO_PADRAO = "Estética Automotiva Rick - Av. Principal, 123";

    /**
     * Cria evento no Google Calendar e persiste no banco de dados
     * Operação atômica: ou sucede completamente ou falha
     */
    @Transactional
    public CalendarioEvento criarEventoAgendamento(OrdemServicoCriadaEvent event) {
        try {
//            log.info("Processando agendamento para ordem {}", event.IdOrdemServico());
//            ordemServicoStatusService.confirmarAgendamento(event.IdOrdemServico());

            CalendarioEventoRequest request = montarEvento(event);
            var response = calendarioPort.criarEvento(request);
            log.info("Evento Google criado para ordem {} (googleId={})", event.IdOrdemServico(), response.getId());

//            CalendarioEvento calendarioEvento = persistenceService.persistirEvento(response, event);
//            log.info("Evento persistido para ordem {} (id={})", event.IdOrdemServico(), calendarioEvento.getId());
            return null;

        } catch (Exception e) {
            log.error("Falha ao criar ou persistir evento para ordem {}", event.IdOrdemServico(), e);
            throw e;
        }
    }

    @Transactional
    public CalendarioEventoAtualizacaoRequest criarEventoAtualizacaoAgendamento(OrdemServicoAtualizadaEvent event) {
        try {
            log.info("Processando agendamento para ordem {}", event.idOrdemServico());
            ordemServicoStatusService.confirmarAgendamento(event.idOrdemServico(), event.status());

            CalendarioEventoAtualizacaoRequest request = montarEventoAtualizacao(event);
            calendarioPort.atualizarEvento(request);
            log.info("Evento Google atualizado para ordem {}", event.idOrdemServico());

//            CalendarioEvento calendarioEvento = persistenceService.persistirEvento(response, event);
//            log.info("Evento persistido para ordem {} (id={})", event.IdOrdemServico(), calendarioEvento.getId());
            return null;

        } catch (Exception e) {
            log.error("Falha ao criar ou persistir evento para ordem {}", event.idOrdemServico(), e);
            throw e;
        }
    }

    /**
     * Monta o objeto de requisição para criar evento no Google Calendar
     */
    private CalendarioEventoRequest montarEvento(OrdemServicoCriadaEvent event) {
        LocalDateTime dataAgendamento = event.dataAgendamento();
        Instant inicio = dataAgendamento.atZone(ZoneId.of(FUSO_SAO_PAULO)).toInstant();
        Instant fim = dataAgendamento.plusHours(1).atZone(ZoneId.of(FUSO_SAO_PAULO)).toInstant();

        String idOrdemServico = event.IdOrdemServico().toString();
        String placa = event.placaVeiculo();
        String titulo = "Atendimento veículo - " + placa;
        String descricao = construirDescricao(event);

        return CalendarioEventoRequest.builder()
                .idOrdemServico(idOrdemServico)
                .titulo(titulo)
                .descricao(descricao)
                .localizacao(LOCALIZACAO_PADRAO)
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .fusoHorario(FUSO_SAO_PAULO)
                .emailsParticipantes(List.of())
                .visibilidade("default")
                .convidadosPodemVerOutrosConvidados(false)
                .convidadosPodemConvidarOutros(false)
                .transparencia("opaque")
                .build();
    }

    private CalendarioEventoAtualizacaoRequest montarEventoAtualizacao(OrdemServicoAtualizadaEvent event) {
        return CalendarioEventoAtualizacaoRequest.builder()
                .idOrdemServico(event.idOrdemServico())
                .dataAgendamento(event.dataAgendamento())
                .precoMinimo(event.precoMinimo())
                .observacoes(event.observacoes())
                .status(event.status())
                .motivo(event.motivo())
                .build();
    }

    /**
     * Constrói a descrição do evento com dados da ordem de serviço
     */
    private String construirDescricao(OrdemServicoCriadaEvent event) {
        StringBuilder descricao = new StringBuilder();

        descricao.append("ID Ordem: ").append(event.IdOrdemServico()).append("\n");
        descricao.append("Placa do Veículo: ").append(event.placaVeiculo()).append("\n\n");

        if (event.servicos() != null && !event.servicos().isEmpty()) {
            descricao.append("Serviços:\n");
            event.servicos().forEach(s -> descricao.append("- ").append(s).append("\n"));
        }

        if (event.observacoes() != null && !event.observacoes().isBlank()) {
            descricao.append("\nObservações: ").append(event.observacoes());
        }

        return descricao.toString();
    }
}
