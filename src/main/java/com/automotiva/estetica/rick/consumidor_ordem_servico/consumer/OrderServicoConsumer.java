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
    private void consumidorOnderServico(OrdemServicoCriadaEvent event) {
        log.info("=== ORDEM DE SERVIÇO CRIADA RECEBIDA ===");
        log.info("ID Ordem: {}", event.IdOrdemServico());
        try {
            CalendarioEvento calendarioEvento = agendamentoCalendarioService.criarEventoAgendamento(event);
            log.info("✅ Processamento concluído com sucesso!");
            log.info("   ID do evento no banco de dados: {}", calendarioEvento.getId());
            log.info("   ID do evento no Google: {}", calendarioEvento.getEventoIdGoogle());
        } catch (Exception e) {
            log.error("❌ Erro ao processar ordem de serviço ID: {}", event.IdOrdemServico(), e);
            throw e;
        }
    }
}
