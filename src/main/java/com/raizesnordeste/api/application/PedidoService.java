package com.raizesnordeste.api.application;

import com.raizesnordeste.api.domain.Estoque;
import com.raizesnordeste.api.domain.ItemPedido;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Pedido.StatusPedido;
import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.infrastructure.EstoqueRepository;
import com.raizesnordeste.api.infrastructure.PedidoRepository;
import com.raizesnordeste.api.infrastructure.ProdutoRepository;
import com.raizesnordeste.api.infrastructure.UsuarioRepository;
import com.raizesnordeste.api.infrastructure.UnidadeRepository;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    public Pedido criarPedido(Pedido pedido) {
        pedido.setCliente(usuarioRepository.findById(pedido.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente nao encontrado!")));
        pedido.setUnidade(unidadeRepository.findById(pedido.getUnidade().getId())
            .orElseThrow(() -> new RuntimeException("Unidade nao encontrada!")));

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto nao encontrado!"));

            item.setProduto(produto); // <- add aqui

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
        Pedido salvo = pedidoRepository.save(pedido);
        return pedidoRepository.findById(salvo.getId()).orElse(salvo);
    }

    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado!"));
        pedido.setStatus(novoStatus);
        Pedido salvo = pedidoRepository.save(pedido);
        return pedidoRepository.findById(salvo.getId()).orElse(salvo);
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

    public List<Pedido> listarPorCanalEStatus(Pedido.CanalPedido canal, Pedido.StatusPedido status) {
        return pedidoRepository.findByCanalPedidoAndStatus(canal, status);
    }
}