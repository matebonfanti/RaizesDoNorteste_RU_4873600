package com.raizesdonordeste.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        String mensagem = ex.getMessage();

        if (mensagem != null && mensagem.contains("não encontrado")) {
            body.put("error", "NAO_ENCONTRADO");
            body.put("message", mensagem);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        
        if (mensagem != null && (mensagem.contains("obrigatório") || mensagem.contains("inválido"))) {
            body.put("error", "ERRO_VALIDACAO");
            body.put("message", mensagem);
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        body.put("error", "ERRO_INTERNO");
        body.put("message", mensagem);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonInvalido(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "JSON_INVALIDO");
        body.put("message", "O corpo da requisição (JSON) está mal formatado ou possui tipos de dados incompatíveis.");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleBancoDeDados(org.springframework.dao.DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "CONFLITO_DADOS");
        body.put("message", "A operação violou uma regra de integridade do banco de dados (ex: exclusão de item em uso).");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}