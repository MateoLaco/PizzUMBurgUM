package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {
    boolean existsByEmail(String email);
    Funcionario findByEmail(String email);
    List<Funcionario> findByRol(String rol);
}
