package com.automotiva.estetica.rick.consumidor_ordem_servico.repository;

import com.automotiva.estetica.rick.consumidor_ordem_servico.entity.CalendarioEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarioEventoRepository extends JpaRepository<CalendarioEvento, Long> {

    /**
     * Encontrar um evento pelo ID do Google Calendar
     */
    Optional<CalendarioEvento> findByEventoIdGoogle(String eventoIdGoogle);

    /**
     * Verificar se um evento Google já foi sincronizado
     */
    boolean existsByEventoIdGoogle(String eventoIdGoogle);
}
