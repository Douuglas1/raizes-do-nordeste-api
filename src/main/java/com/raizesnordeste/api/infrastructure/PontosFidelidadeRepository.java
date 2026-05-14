package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.PontosFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PontosFidelidadeRepository extends JpaRepository<PontosFidelidade, Long> {

    List<PontosFidelidade> findByClienteId(Long clienteId);

    @Query("SELECT SUM(p.pontos) FROM PontosFidelidade p WHERE p.cliente.id = :clienteId AND p.tipo = 'CREDITO'")
    Integer totalCreditosByClienteId(Long clienteId);

    @Query("SELECT SUM(p.pontos) FROM PontosFidelidade p WHERE p.cliente.id = :clienteId AND p.tipo = 'DEBITO'")
    Integer totalDebitosByClienteId(Long clienteId);
}