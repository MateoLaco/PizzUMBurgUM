package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

        // ahora tipo es String, no char
        if (producto.getTipo() == null || producto.getTipo().isBlank()) {
            throw new RuntimeException("El tipo de producto es obligatorio");
        }

        // (opcional pero prolijo) normalizar a una sola letra mayúscula
        producto.setTipo(producto.getTipo().trim().substring(0, 1).toUpperCase());

        // siempre lo dejamos activo al crearlo
        producto.setActivo(true);

        return productoRepositorio.save(producto);
    }


    public void actualizarActivo(Long idProducto, boolean activo) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

        producto.setActivo(activo);
        productoRepositorio.save(producto);
    }

    public void actualizarActivosEnLote(List<Long> idsActivos) {

        // Si la lista viene null, lo tomamos como "ninguno activo"
        Set<Long> activosSet = (idsActivos == null) ? Set.of() : new java.util.HashSet<>(idsActivos);

        List<Producto> todos = productoRepositorio.findAll();

        for (Producto p : todos) {
            boolean debeEstarActivo = activosSet.contains(p.getId_producto());
            p.setActivo(debeEstarActivo);
        }

        productoRepositorio.saveAll(todos);
    }

}
