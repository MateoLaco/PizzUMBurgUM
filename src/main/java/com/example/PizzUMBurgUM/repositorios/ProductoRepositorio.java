package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findByTipo(String tipo);
}