package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.tipo = :tipo AND p.activo = true")
    List<Producto> findByTipoAndActivoTrue(@Param("tipo")String tipo);
}