package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedidoId(Long pedidoId);
}