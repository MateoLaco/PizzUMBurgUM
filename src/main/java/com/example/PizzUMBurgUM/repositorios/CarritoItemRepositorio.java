package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.CarritoItem;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Creacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepositorio extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByCliente(Cliente cliente);
    List<CarritoItem> findByClienteOrderByFechaAgregadoDesc(Cliente cliente);
    Optional<CarritoItem> findByClienteAndCreacion(Cliente cliente, Creacion creacion);
    void deleteByCliente(Cliente cliente);
}
