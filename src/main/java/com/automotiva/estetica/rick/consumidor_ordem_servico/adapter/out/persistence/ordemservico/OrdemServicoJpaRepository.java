package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.ordemservico;

import com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.jpaentity.OrdemServicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoJpaEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ordem_servico SET fk_status = :statusId WHERE id = :ordemId", nativeQuery = true)
    int atualizarStatus(@Param("ordemId") Long ordemId, @Param("statusId") Long statusId);
}
