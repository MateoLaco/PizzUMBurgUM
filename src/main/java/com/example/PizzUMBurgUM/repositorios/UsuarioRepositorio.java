package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
