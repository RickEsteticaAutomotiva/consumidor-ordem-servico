package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.jpaentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(name = "preco_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoMinimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_status", nullable = false)
    private StatusJpaEntity status;

    @Column(name = "observacoes", length = 255)
    private String observacoes;

    @Column(name = "dt_conclusao")
    private LocalDateTime dtConclusao;
}
