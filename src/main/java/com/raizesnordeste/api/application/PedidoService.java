package com.raizesnordeste.api.application;

import com.raizesnordeste.api.domain.Estoque;
import com.raizesnordeste.api.domain.ItemPedido;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Pedido.StatusPedido;
import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.infrastructure.EstoqueRepository;
import com.raizesnordeste.api.infrastructure.PedidoRepository;
import com.raizesnordeste.api.infrastructure.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public Pedido criarPedido(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto nao encontrado!"));

            Estoque estoque = estoqueRepository
                    .findByProdutoIdAndUnidadeId(produto.getId(), pedido.getUnidade().getId())
                    .orElseThrow(() -> new RuntimeException("Estoque nao encontrado!"));

            if (estoque.getQuantidade() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            estoque.setQuantidade(estoque.getQuantidade() - item.getQuantidade());
            estoqueRepository.save(estoque);

            item.setPedido(pedido);
            item.setPrecoUnitario(produto.getPreco());
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado!"));
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCanal(Pedido.CanalPedido canal) {
        return pedidoRepository.findByCanalPedido(canal);
    }

    public List<Pedido> listarPorClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
}