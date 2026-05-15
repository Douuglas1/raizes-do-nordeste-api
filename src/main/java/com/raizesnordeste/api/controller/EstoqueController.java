package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.application.EstoqueService;
import com.raizesnordeste.api.domain.Estoque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping
    public ResponseEntity<?> adicionarEstoque(@RequestBody Estoque estoque) {
        try {
            Estoque novo = estoqueService.adicionarEstoque(estoque);
            return ResponseEntity.status(201).body(novo);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_ADICIONAR_ESTOQUE");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @GetMapping("/{produtoId}/{unidadeId}")
    public ResponseEntity<?> consultarEstoque(@PathVariable Long produtoId,
                                               @PathVariable Long unidadeId) {
        try {
            Estoque estoque = estoqueService.consultarEstoque(produtoId, unidadeId);
            return ResponseEntity.ok(estoque);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ESTOQUE_NAO_ENCONTRADO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(404).body(erro);
        }
    }
}