package com.raizesdonordeste.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        // Substituir mock estático por consulta na tabela de Usuários na próxima release.
        if ("gerente@raizes.com".equals(email) && "123456".equals(senha)) {
            return ResponseEntity.ok(Map.of(
                "accessToken", "jwt-token-valido-gerente-12345",
                "perfil", "GERENTE"
            ));
        } else if ("cliente@raizes.com".equals(email) && "123456".equals(senha)) {
            return ResponseEntity.ok(Map.of(
                "accessToken", "jwt-token-valido-cliente-67890",
                "perfil", "CLIENTE"
            ));
        }

        //Retorna 401 (Unauthorized) caso as credenciais sejam inválidas.
        return ResponseEntity.status(401).body(Map.of(
            "error", "CREDENCIAIS_INVALIDAS",
            "message", "E-mail ou senha inválidos."
        ));
    }
}