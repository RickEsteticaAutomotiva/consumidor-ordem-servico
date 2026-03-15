package com.automotiva.estetica.rick.consumidor_ordem_servico.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "calendario_evento"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fk_ordem_servico", nullable = false)
    private Long idOrdemServico;

    @Column(name = "evento_id_google", nullable = false, unique = true, length = 255)
    private String eventoIdGoogle;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "LONGTEXT")
    private String descricao;

    @Column(name = "localizacao", length = 500)
    private String localizacao;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "fuso_horario", length = 50)
    private String fusoHorario;

    @Column(name = "placa_veiculo", length = 20)
    private String placaVeiculo;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;
}
