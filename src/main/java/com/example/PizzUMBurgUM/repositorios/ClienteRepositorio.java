package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente,Long> {
    boolean existsByEmail(String email);
}
