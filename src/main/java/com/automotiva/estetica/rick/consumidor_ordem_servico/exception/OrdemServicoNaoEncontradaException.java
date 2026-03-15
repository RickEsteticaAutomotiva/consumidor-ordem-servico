package com.automotiva.estetica.rick.consumidor_ordem_servico.exception;

public class OrdemServicoNaoEncontradaException extends RuntimeException {

    public OrdemServicoNaoEncontradaException(Long id) {
        super("Ordem de serviço não encontrada: " + id);
    }
}

