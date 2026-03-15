package com.automotiva.estetica.rick.consumidor_ordem_servico.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEventoResponse {
    private String id;
    private String titulo;
    private String descricao;
    private String localizacao;
    private Instant dataHoraInicio;
    private Instant dataHoraFim;
}

