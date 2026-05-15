package com.raizesnordeste.api.application;

import com.raizesnordeste.api.domain.PontosFidelidade;
import com.raizesnordeste.api.domain.PontosFidelidade.TipoMovimentacao;
import com.raizesnordeste.api.domain.Usuario;
import com.raizesnordeste.api.infrastructure.PontosFidelidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FidelidadeService {

    @Autowired
    private PontosFidelidadeRepository pontosFidelidadeRepository;

    public PontosFidelidade adicionarPontos(Usuario cliente, Integer pontos, String descricao) {
        PontosFidelidade movimento = new PontosFidelidade();
        movimento.setCliente(cliente);
        movimento.setPontos(pontos);
        movimento.setTipo(TipoMovimentacao.CREDITO);
        movimento.setDescricao(descricao);
        return pontosFidelidadeRepository.save(movimento);
    }

    public PontosFidelidade resgatarPontos(Usuario cliente, Integer pontos, String descricao) {
        Integer saldo = consultarSaldo(cliente.getId());
        if (saldo < pontos) {
            throw new RuntimeException("Saldo de pontos insuficiente!");
        }
        PontosFidelidade movimento = new PontosFidelidade();
        movimento.setCliente(cliente);
        movimento.setPontos(pontos);
        movimento.setTipo(TipoMovimentacao.DEBITO);
        movimento.setDescricao(descricao);
        return pontosFidelidadeRepository.save(movimento);
    }

    public Integer consultarSaldo(Long clienteId) {
        Integer creditos = pontosFidelidadeRepository.totalCreditosByClienteId(clienteId);
        Integer debitos = pontosFidelidadeRepository.totalDebitosByClienteId(clienteId);
        creditos = creditos == null ? 0 : creditos;
        debitos = debitos == null ? 0 : debitos;
        return creditos - debitos;
    }

    public List<PontosFidelidade> historico(Long clienteId) {
        return pontosFidelidadeRepository.findByClienteId(clienteId);
    }
}