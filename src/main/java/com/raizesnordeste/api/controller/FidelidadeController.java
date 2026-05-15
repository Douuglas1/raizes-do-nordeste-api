package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.application.FidelidadeService;
import com.raizesnordeste.api.application.UsuarioService;
import com.raizesnordeste.api.domain.PontosFidelidade;
import com.raizesnordeste.api.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeService fidelidadeService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/saldo/{clienteId}")
    public ResponseEntity<?> consultarSaldo(@PathVariable Long clienteId) {
        try {
            Integer saldo = fidelidadeService.consultarSaldo(clienteId);
            Map<String, Object> response = new HashMap<>();
            response.put("clienteId", clienteId);
            response.put("saldo", saldo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_CONSULTAR_SALDO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(404).body(erro);
        }
    }

    @GetMapping("/historico/{clienteId}")
    public ResponseEntity<List<PontosFidelidade>> historico(@PathVariable Long clienteId) {
        return ResponseEntity.ok(fidelidadeService.historico(clienteId));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarPontos(@RequestBody Map<String, Object> request) {
        try {
            Long clienteId = Long.valueOf(request.get("clienteId").toString());
            Integer pontos = Integer.valueOf(request.get("pontos").toString());
            String descricao = request.get("descricao").toString();

            Usuario cliente = usuarioService.buscarPorId(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente nao encontrado!"));

            PontosFidelidade movimento = fidelidadeService.adicionarPontos(cliente, pontos, descricao);
            return ResponseEntity.status(201).body(movimento);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_ADICIONAR_PONTOS");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @PostMapping("/resgatar")
    public ResponseEntity<?> resgatarPontos(@RequestBody Map<String, Object> request) {
        try {
            Long clienteId = Long.valueOf(request.get("clienteId").toString());
            Integer pontos = Integer.valueOf(request.get("pontos").toString());
            String descricao = request.get("descricao").toString();

            Usuario cliente = usuarioService.buscarPorId(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente nao encontrado!"));

            PontosFidelidade movimento = fidelidadeService.resgatarPontos(cliente, pontos, descricao);
            return ResponseEntity.status(201).body(movimento);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "ERRO_AO_RESGATAR_PONTOS");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }
}