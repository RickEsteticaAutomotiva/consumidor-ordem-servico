package com.automotiva.estetica.rick.consumidor_ordem_servico.consumer;

import com.automotiva.estetica.rick.constantes.RabbitMqConsts;
import com.automotiva.estetica.rick.dto.OrdemServicoCriadaEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderServicoConsumer {
    @RabbitListener(queues = RabbitMqConsts.ORDEM_SERVICO_CRIADA_QUEUE)
    private void consumidor(OrdemServicoCriadaEvent event) {
        System.out.println("Ordem de Serviço Criada: " + event);
    }
}
