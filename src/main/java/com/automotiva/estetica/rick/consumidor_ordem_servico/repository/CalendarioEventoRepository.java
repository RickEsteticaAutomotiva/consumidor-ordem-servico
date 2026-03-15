package com.automotiva.estetica.rick.consumidor_ordem_servico.repository;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarioEventoRepository extends JpaRepository<CalendarioEvento, Long> {

    /**
     * Encontrar um evento pelo ID do Google Calendar
     */
    Optional<CalendarioEvento> findByEventoIdGoogle(String eventoIdGoogle);

    /**
     * Encontrar todos os eventos associados a uma ordem de serviço
     */
    List<CalendarioEvento> findByIdOrdemServico(Long idOrdemServico);

    /**
     * Contar eventos para uma ordem de serviço
     */
    Long countByIdOrdemServico(Long idOrdemServico);

    /**
     * Verificar se um evento Google já foi sincronizado
     */
    boolean existsByEventoIdGoogle(String eventoIdGoogle);
}

