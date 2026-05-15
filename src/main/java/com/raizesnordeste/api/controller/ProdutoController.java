package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.infrastructure.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<Produto>> listarPorUnidade(@PathVariable Long unidadeId) {
        return ResponseEntity.ok(produtoRepository.findByUnidadeIdAndDisponivelTrue(unidadeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        try {
            Produto novo = produtoRepository.save(produto);
            return ResponseEntity.status(201).body(novo);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_CRIAR_PRODUTO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return produtoRepository.findById(id).map(p -> {
            p.setNome(produto.getNome());
            p.setDescricao(produto.getDescricao());
            p.setPreco(produto.getPreco());
            p.setCategoria(produto.getCategoria());
            p.setDisponivel(produto.getDisponivel());
            return ResponseEntity.ok(produtoRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }
}