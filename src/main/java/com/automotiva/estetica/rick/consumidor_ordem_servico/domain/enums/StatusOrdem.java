package com.automotiva.estetica.rick.consumidor_ordem_servico.domain.enums;

import java.util.Arrays;

public enum StatusOrdem {
    AGUARDANDO(1L),
    AGENDA_CONFIRMADA(2L),
    AGUARDANDO_PECAS(3L),
    CANCELADO(4L),
    CONCLUIDO(5L);

    private final Long id;

    StatusOrdem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static StatusOrdem fromId(Long id) {
        if (id == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(status -> status.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static boolean requerNotificacao(Long statusId) {
        StatusOrdem statusOrdem = fromId(statusId);
        return statusOrdem == AGENDA_CONFIRMADA || statusOrdem == CONCLUIDO;
    }
}

