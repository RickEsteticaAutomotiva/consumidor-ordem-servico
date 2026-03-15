package com.automotiva.estetica.rick.consumidor_ordem_servico.service;

import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.entity.OrdemServico;
import com.automotiva.estetica.rick.consumidor_ordem_servico.domain.enums.StatusOrdem;
import com.automotiva.estetica.rick.consumidor_ordem_servico.exception.OrdemServicoNaoEncontradaException;
import com.automotiva.estetica.rick.consumidor_ordem_servico.port.OrdemServicoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdemServicoStatusService {

    private final OrdemServicoRepositoryPort ordemServicoRepositoryPort;

    @Transactional
    public void confirmarAgendamento(Long ordemServicoId) {
        if (ordemServicoId == null) {
            throw new IllegalArgumentException("Id da ordem de serviço não pode ser nulo");
        }

        log.info("Atualizando status da ordem {} para AGENDA_CONFIRMADA", ordemServicoId);
        ordemServicoRepositoryPort.atualizarStatus(ordemServicoId, StatusOrdem.AGENDA_CONFIRMADA.getId());
        log.info("Status da ordem {} atualizado com sucesso", ordemServicoId);
    }

    @Transactional(readOnly = true)
    public OrdemServico buscarOrdem(Long id) {
        return ordemServicoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new OrdemServicoNaoEncontradaException(id));
    }
}

