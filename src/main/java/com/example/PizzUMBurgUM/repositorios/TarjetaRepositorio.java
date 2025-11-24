package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Tarjeta;
import com.example.PizzUMBurgUM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarjetaRepositorio extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByCliente(Cliente cliente);
    List<Tarjeta> findByClienteAndPrincipal(Cliente cliente, Boolean principal);
    void deleteByClienteAndId(Cliente cliente, Long id);
}