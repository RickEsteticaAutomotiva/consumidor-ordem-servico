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
    public void confirmarAgendamento(Long ordemServicoId, Long statusOrdemServico) {
        if (statusOrdemServico == null) {
            log.info("A Ordem de serviço está sem status para atualização");
            return;
        }

        Long statusId = 0L;
        switch (statusOrdemServico.intValue()) {
            case 1 -> statusId = 1L;
            case 2 -> statusId = 2L;
            case 3 -> statusId = 3L;
            case 4 -> statusId = 4L;
            case 5 -> statusId = 5L;
        }

        log.info("Atualizando status da ordem {} | status: {}", ordemServicoId, statusId);
        ordemServicoRepositoryPort.atualizarStatus(ordemServicoId, statusId);
        log.info("Status da ordem {} atualizado com sucesso", ordemServicoId);
    }

    @Transactional(readOnly = true)
    public OrdemServico buscarOrdem(Long id) {
        return ordemServicoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new OrdemServicoNaoEncontradaException(id));
    }
}

