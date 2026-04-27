package com.raizesdonordeste.api.repositories;

import com.raizesdonordeste.api.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
   
}