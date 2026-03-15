package com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.ordemservico;

import com.automotiva.estetica.rick.consumidor_ordem_servico.adapter.out.persistence.mapper.OrdemServicoPersistenceMapper;
import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.entity.OrdemServico;
import com.automotiva.estetica.rick.consumidor_ordem_servico.exception.OrdemServicoNaoEncontradaException;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.OrdemServicoRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrdemServicoRepositoryAdapter implements OrdemServicoRepositoryPort {

    private final OrdemServicoJpaRepository jpaRepository;
    private final OrdemServicoPersistenceMapper mapper;

    @Override
    public OrdemServico salvar(OrdemServico ordemServico) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpaEntity(ordemServico)));
    }

    @Override
    public Optional<OrdemServico> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void atualizarStatus(Long ordemServicoId, Long statusId) {
        int linhas = jpaRepository.atualizarStatus(ordemServicoId, statusId);
        if (linhas == 0) {
            throw new OrdemServicoNaoEncontradaException(ordemServicoId);
        }
    }
}
