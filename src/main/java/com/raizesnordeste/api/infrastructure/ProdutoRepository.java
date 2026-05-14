package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByUnidadeId(Long unidadeId);

    List<Produto> findByUnidadeIdAndDisponivelTrue(Long unidadeId);
}