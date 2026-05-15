package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.domain.Unidade;
import com.raizesnordeste.api.infrastructure.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @GetMapping
    public ResponseEntity<List<Unidade>> listar() {
        return ResponseEntity.ok(unidadeRepository.findByAtivaTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return unidadeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Unidade unidade) {
        try {
            Unidade nova = unidadeRepository.save(unidade);
            return ResponseEntity.status(201).body(nova);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_CRIAR_UNIDADE");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }
}