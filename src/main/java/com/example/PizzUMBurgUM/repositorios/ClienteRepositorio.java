package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente,Long> {
    boolean existsByEmail(String email);
    Cliente findByEmail(String email);

}
