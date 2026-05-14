package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    List<Unidade> findByAtivaTrue();
}