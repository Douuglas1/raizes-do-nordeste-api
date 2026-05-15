package com.raizesnordeste.api.application;

import com.raizesnordeste.api.domain.Pagamento;
import com.raizesnordeste.api.domain.Pagamento.StatusPagamento;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Pedido.StatusPedido;
import com.raizesnordeste.api.infrastructure.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoService pedidoService;

    public Pagamento processarPagamentoMock(Pedido pedido, Pagamento.FormaPagamento formaPagamento) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getTotal());
        pagamento.setFormaPagamento(formaPagamento);

        boolean aprovado = Math.random() > 0.2;

        if (aprovado) {
            pagamento.setStatus(StatusPagamento.APROVADO);
            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.EM_PREPARO);
        } else {
            pagamento.setStatus(StatusPagamento.RECUSADO);
            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.CANCELADO);
        }

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento buscarPorPedidoId(Long pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado!"));
    }
}