package com.raizesnordeste.api.application;

import com.raizesnordeste.api.domain.Estoque;
import com.raizesnordeste.api.infrastructure.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    public Estoque adicionarEstoque(Estoque estoque) {
        Optional<Estoque> estoqueExistente = estoqueRepository
                .findByProdutoIdAndUnidadeId(
                        estoque.getProduto().getId(),
                        estoque.getUnidade().getId());

        if (estoqueExistente.isPresent()) {
            Estoque e = estoqueExistente.get();
            e.setQuantidade(e.getQuantidade() + estoque.getQuantidade());
            return estoqueRepository.save(e);
        }

        return estoqueRepository.save(estoque);
    }

    public Estoque consultarEstoque(Long produtoId, Long unidadeId) {
        return estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId)
                .orElseThrow(() -> new RuntimeException("Estoque nao encontrado!"));
    }
}