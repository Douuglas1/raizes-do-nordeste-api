package com.raizesnordeste.api.controller;

import com.raizesnordeste.api.application.JwtService;
import com.raizesnordeste.api.application.UsuarioService;
import com.raizesnordeste.api.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String senha = request.get("senha");

        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "CREDENCIAIS_INVALIDAS");
            erro.put("message", "E-mail ou senha invalidos.");
            return ResponseEntity.status(401).body(erro);
        }

        String token = jwtService.gerarToken(email);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 86400);
        response.put("usuario", Map.of(
            "id", usuario.getId(),
            "nome", usuario.getNome(),
            "perfil", usuario.getPerfil()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Usuario usuario) {
        try {
            Usuario novo = usuarioService.cadastrar(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("id", novo.getId());
            response.put("nome", novo.getNome());
            response.put("email", novo.getEmail());
            response.put("perfil", novo.getPerfil());
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("error", "EMAIL_JA_CADASTRADO");
            erro.put("message", e.getMessage());
            return ResponseEntity.status(409).body(erro);
        }
    }
}