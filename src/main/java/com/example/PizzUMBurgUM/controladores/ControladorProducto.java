package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.servicios.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ControladorProducto {

    private final ProductoServicio productoServicio;

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoServicio.obtenerTodosProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productoServicio.obtenerProductoPorId(id));
    }
}