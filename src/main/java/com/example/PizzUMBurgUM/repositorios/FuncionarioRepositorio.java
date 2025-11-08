package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {
    public Funcionario findByEmail(String email);
}
