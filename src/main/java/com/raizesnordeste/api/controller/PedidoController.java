package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.application.PedidoService;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Pedido.CanalPedido;
import com.raizesnordeste.api.domain.Pedido.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido novo = pedidoService.criarPedido(pedido);
            return ResponseEntity.status(201).body(novo);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_CRIAR_PEDIDO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(409).body(erro);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar(
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(required = false) StatusPedido status) {

        if (canalPedido != null && status != null) {
            return ResponseEntity.ok(pedidoService.listarPorCanalEStatus(canalPedido, status));
        } else if (canalPedido != null) {
            return ResponseEntity.ok(pedidoService.listarPorCanal(canalPedido));
        }
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> request) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(request.get("status"));
            Pedido atualizado = pedidoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_ATUALIZAR_STATUS");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }
}