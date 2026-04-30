package com.raizesdonordeste.api.services;

import com.raizesdonordeste.api.models.ItemPedido;
import com.raizesdonordeste.api.models.Pedido;
import com.raizesdonordeste.api.models.Produto;
import com.raizesdonordeste.api.models.enums.StatusPedido;
import com.raizesdonordeste.api.repositories.PedidoRepository;
import com.raizesdonordeste.api.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PedidoService {

    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Pedido realizarPedido(Pedido pedido) {
        
        
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        BigDecimal totalPedido = BigDecimal.ZERO;

        
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                
                
                Produto produto = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + item.getProduto().getId()));

               
                item.setPrecoUnitario(produto.getPrecoBase());

               
                item.setPedido(pedido);

                
                BigDecimal quantidade = new BigDecimal(item.getQuantidade());
                BigDecimal subtotal = item.getPrecoUnitario().multiply(quantidade);
                
                
                totalPedido = totalPedido.add(subtotal);
            }
        }

      
        pedido.setTotal(totalPedido);

        
        if (pedido.getCliente() != null && pedido.getConsentimentoLgpd() == null) {
            pedido.setConsentimentoLgpd(false); 
        }

        if (pedido.getCanalPedido() == null) {
            throw new RuntimeException("O canal de origem do pedido (APP, WEB, etc) é obrigatório!");
}

        
        return pedidoRepository.save(pedido);
    }
}