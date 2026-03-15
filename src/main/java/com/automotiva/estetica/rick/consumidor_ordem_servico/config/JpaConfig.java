package com.automotiva.estetica.rick.consumidor_ordem_servico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.automotiva.estetica.rick.consumidor_ordem_servico.repository"
)
@EnableTransactionManagement
public class JpaConfig {
    // Configuração JPA com suporte a transações
}

