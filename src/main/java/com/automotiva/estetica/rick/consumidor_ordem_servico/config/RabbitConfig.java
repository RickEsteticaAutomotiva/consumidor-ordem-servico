package com.automotiva.estetica.rick.consumidor_ordem_servico.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        SimpleMessageConverter converter = new SimpleMessageConverter();
        // Permite desserializacao apenas de classes/pacotes confiaveis.
        converter.setAllowedListPatterns(List.of(
                "com.automotiva.estetica.rick.dto.*",
                "com.automotiva.estetica.rick.constantes.*",
                "java.time.*",
                "java.util.*"
        ));

        factory.setMessageConverter(converter);
        return factory;
    }
}
