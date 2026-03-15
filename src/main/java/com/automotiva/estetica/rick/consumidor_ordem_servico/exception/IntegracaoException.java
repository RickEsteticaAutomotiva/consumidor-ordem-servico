package com.automotiva.estetica.rick.consumidor_ordem_servico.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegracaoException extends RuntimeException {
    private String mensagem;
    private String detalhes;

    @Override
    public String getMessage() {
        return mensagem + (detalhes != null ? " - " + detalhes : "");
    }
}

