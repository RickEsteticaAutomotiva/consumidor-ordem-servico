package com.automotiva.estetica.rick.consumidor_ordem_servico.consumer;

import com.automotiva.estetica.rick.constantes.RabbitMqConsts;
import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import com.automotiva.estetica.rick.consumidor_ordem_servico.service.AgendamentoCalendarioService;
import com.automotiva.estetica.rick.dto.OrdemServicoAtualizadaEvent;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdemServicoAtualizacaoConsumer {
    private final AgendamentoCalendarioService agendamentoCalendarioService;

    @RabbitListener(queues = RabbitMqConsts.ORDEM_SERVICO_ATUALIZADA_QUEUE)
    private void consumidorOrdemServicoAtualizacao(OrdemServicoAtualizadaEvent event) {
        log.info("Ordem de serviço {} recebida para atualização", event.idOrdemServico());
        try {
            log.info("criando evento de atualização para ordem de serviço {}", event.idOrdemServico());
            agendamentoCalendarioService.criarEventoAtualizacaoAgendamento(event);
        } catch (Exception e) {
            log.error("Erro ao processar ordem de serviço {}", event.idOrdemServico(), e);
            throw e;
        }
    }
}
