package com.raizesnordeste.api.infrastructure;

import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Pedido.CanalPedido;
import com.raizesnordeste.api.domain.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByCanalPedido(CanalPedido canalPedido);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByCanalPedidoAndStatus(CanalPedido canalPedido, StatusPedido status);
}