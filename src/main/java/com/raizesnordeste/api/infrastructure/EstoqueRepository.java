package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByProdutoIdAndUnidadeId(Long produtoId, Long unidadeId);
}