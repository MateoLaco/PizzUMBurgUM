package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetodoPagoRepositorio extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByClienteOrderByPrincipalDescFechaAltaDesc(Cliente cliente);
    Optional<MetodoPago> findByClienteAndPrincipalTrue(Cliente cliente);
}
