package com.automotiva.estetica.rick.consumidor_ordem_servico.port;

import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.entity.OrdemServico;
import java.util.Optional;

public interface OrdemServicoRepositoryPort {

    OrdemServico salvar(OrdemServico ordemServico);

    Optional<OrdemServico> buscarPorId(Long id);

    void atualizarStatus(Long ordemServicoId, Long statusId);
}
