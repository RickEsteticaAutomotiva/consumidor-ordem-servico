package com.automotiva.estetica.rick.consumidor_ordem_servico.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEventoAtualizacaoRequest {
    private Long idOrdemServico;
    private LocalDateTime dataAgendamento;
    private BigDecimal precoMinimo;
    private String observacoes;
    private Long status;
    private Long motivo;
}
