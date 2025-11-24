package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Direccion;
import com.example.PizzUMBurgUM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DireccionRepositorio extends JpaRepository<Direccion, Long> {
    List<Direccion> findByCliente(Cliente cliente);
    List<Direccion> findByClienteAndPrincipal(Cliente cliente, Boolean principal);
    void deleteByClienteAndId(Cliente cliente, Long id);
}