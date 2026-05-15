package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.application.PagamentoService;
import com.raizesnordeste.api.application.PedidoService;
import com.raizesnordeste.api.domain.Pagamento;
import com.raizesnordeste.api.domain.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/processar/{pedidoId}")
    public ResponseEntity<?> processarPagamento(@PathVariable Long pedidoId,
                                                 @RequestBody Map<String, String> request) {
        try {
            Pedido pedido = pedidoService.buscarPorId(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido nao encontrado!"));

            Pagamento.FormaPagamento forma = Pagamento.FormaPagamento
                    .valueOf(request.get("formaPagamento"));

            Pagamento pagamento = pagamentoService.processarPagamentoMock(pedido, forma);

            Map<String, Object> response = new HashMap<>();
            response.put("pagamentoId", pagamento.getId());
            response.put("status", pagamento.getStatus());
            response.put("valor", pagamento.getValor());
            response.put("formaPagamento", pagamento.getFormaPagamento());
            response.put("statusPedido", pedido.getStatus());

            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_PROCESSAR_PAGAMENTO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<?> buscarPorPedido(@PathVariable Long pedidoId) {
        try {
            Pagamento pagamento = pagamentoService.buscarPorPedidoId(pedidoId);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "PAGAMENTO_NAO_ENCONTRADO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(404).body(erro);
        }
    }
}