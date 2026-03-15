package com.automotiva.estetica.rick.consumidor_ordem_servico.domain.entity;

import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.enums.StatusOrdem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    private Long id;
    private LocalDateTime dataAgendamento;
    private BigDecimal precoMinimo;
    private Status status;
    private String observacoes;
    private LocalDateTime dtConclusao;

    public void atualizarStatus(StatusOrdem statusOrdem) {
        if (statusOrdem == null) {
            return;
        }
        this.status = Status.builder().id(statusOrdem.getId()).build();
        if (StatusOrdem.CONCLUIDO == statusOrdem) {
            this.dtConclusao = LocalDateTime.now();
        }
    }
}
