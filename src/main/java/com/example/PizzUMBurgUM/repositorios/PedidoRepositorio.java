package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

    long countByEstado(String estado);

    long countByEstadoAndFecha(String estado, LocalDate fecha);

    // Para la tabla de gestión de pedidos
    List<Pedido> findByEstado(String estado);

    List<Pedido> findByFecha(LocalDate fecha);

    List<Pedido> findByEstadoAndFecha(String estado, LocalDate fecha);

    Optional<Pedido> findTopByClienteAndEstadoInOrderByFechaDesc(Cliente cliente, List<String> estados);
}
