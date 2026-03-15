package com.automotiva.estetica.rick.consumidor_ordem_servico.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEventoRequest {
    private String titulo;
    private String descricao;
    private String localizacao;
    private Instant dataHoraInicio;
    private Instant dataHoraFim;
    private String fusoHorario;
    private List<String> emailsParticipantes;
    private String visibilidade;
    private Boolean convidadosPodemVerOutrosConvidados;
    private Boolean convidadosPodemConvidarOutros;
    private String transparencia;
}

