package com.automotiva.estetica.rick.consumidor_ordem_servico.port;

public interface CalendarioPort {
    CalendarioEventoResponse criarEvento(CalendarioEventoRequest request);

    void atualizarEvento(CalendarioEventoAtualizacaoRequest request);
}

