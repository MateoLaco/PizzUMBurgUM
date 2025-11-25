package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public List<Producto> listarTodos() {
        return productoRepositorio.findAll();
    }

    public Producto crearProducto(Producto producto) {

        if (producto == null) {
            throw new RuntimeException("El producto no puede ser null");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() < 0) {
            throw new RuntimeException("El precio debe ser mayor o igual a cero");
        }

        if (producto.getTipo() == null || producto.getTipo().isBlank()) {
            throw new RuntimeException("El tipo de producto es obligatorio");
        }

        // normalizamos tipo a una sola letra mayúscula
        producto.setTipo(producto.getTipo().trim().substring(0, 1).toUpperCase());

        // siempre activo al crearlo
        producto.setActivo(true);

        return productoRepositorio.save(producto);
    }

    // ⬇⬇⬇ NUEVO: actualizar precios + activos en lote
    public void actualizarPreciosYActivosEnLote(List<Long> idsActivos,
                                                Map<String, String> requestParams) {

        Set<Long> activosSet = (idsActivos == null)
                ? Collections.emptySet()
                : new HashSet<>(idsActivos);

        List<Producto> todos = productoRepositorio.findAll();

        for (Producto p : todos) {

            // ----- activo/inactivo -----
            p.setActivo(activosSet.contains(p.getId_producto()));

            // ----- precio -----
            String key = "precio_" + p.getId_producto(); // 👈 misma convención que en el HTML
            String valor = requestParams.get(key);

            if (valor != null && !valor.isBlank()) {
                try {
                    float precio = Float.parseFloat(valor.replace(",", "."));
                    if (precio < 0) {
                        throw new IllegalArgumentException(
                                "El precio no puede ser negativo (producto ID " + p.getId_producto() + ")"
                        );
                    }
                    p.setPrecio(precio);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Precio inválido para el producto ID " + p.getId_producto()
                    );
                }
            }
        }

        productoRepositorio.saveAll(todos);
    }

}
