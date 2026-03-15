package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.mapper;

import com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.jpaentity.OrdemServicoJpaEntity;
import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.entity.OrdemServico;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrdemServicoPersistenceMapper {

    OrdemServico toDomain(OrdemServicoJpaEntity entity);

    OrdemServicoJpaEntity toJpaEntity(OrdemServico domain);

    List<OrdemServico> toDomainList(List<OrdemServicoJpaEntity> entities);
}

