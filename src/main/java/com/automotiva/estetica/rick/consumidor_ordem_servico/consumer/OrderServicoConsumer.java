package com.automotiva.estetica.rick.consumidor_ordem_servico.consumer;

import com.automotiva.estetica.rick.constantes.RabbitMqConsts;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.service.AgendamentoCalendarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServicoConsumer {

    private final AgendamentoCalendarioService agendamentoCalendarioService;

    @RabbitListener(queues = RabbitMqConsts.ORDEM_SERVICO_CRIADA_QUEUE)
    private void consumidorOrdemServico(OrdemServicoCriadaEvent event) {
        log.info("Ordem de serviço {} recebida", event.IdOrdemServico());
        try {
            CalendarioEvento calendarioEvento = agendamentoCalendarioService.criarEventoAgendamento(event);
            log.info("Evento criado para ordem {} (dbId={}, googleId={})",
                    calendarioEvento.getIdOrdemServico(), calendarioEvento.getId(), calendarioEvento.getEventoIdGoogle());
        } catch (Exception e) {
            log.error("Erro ao processar ordem de serviço {}", event.IdOrdemServico(), e);
            throw e;
        }
    }
}
