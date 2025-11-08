package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepositorio extends JpaRepository<Pedido,Long> {
}
