package com.raizesdonordeste.api.controllers;

import com.raizesdonordeste.api.models.Pedido;
import com.raizesdonordeste.api.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.raizesdonordeste.api.models.enums.StatusPedido;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<Object> processarPagamento(@RequestBody Map<String, Long> payload) {
        Long pedidoId = payload.get("pedidoId");

        if (pedidoId == null) {
            return ResponseEntity.badRequest().body("O campo pedidoId é obrigatório.");
        }

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Pedido não encontrado.");
        }

        Pedido pedido = pedidoOpt.get();
        

        pedido.setStatus(StatusPedido.PREPARANDO);
        pedidoRepository.save(pedido);

        return ResponseEntity.ok().body(Map.of(
            "mensagem", "Pagamento aprovado com sucesso!",
            "pedidoId", pedido.getId(),
            "novoStatus", pedido.getStatus()
        ));
    }
}