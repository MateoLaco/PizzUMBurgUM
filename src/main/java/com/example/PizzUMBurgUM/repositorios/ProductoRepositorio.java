package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepositorio extends JpaRepository<Producto, Integer> {
}