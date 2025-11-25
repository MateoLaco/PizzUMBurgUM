package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreacionHamburguesaServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    // C = carne
    public List<Producto> obtenerCarnes() {
        return productoRepositorio.findByTipoAndActivoTrue("C");
    }

    // PH = pan hamburguesa
    public List<Producto> obtenerPanesHamburguesa() {
        return productoRepositorio.findByTipoAndActivoTrue("PH");
    }

    // H = ingredientes (lechuga, tomate, cheddar, etc.)
    public List<Producto> obtenerIngredientesHamburguesa() {
        return productoRepositorio.findByTipoAndActivoTrue("H");
    }

    // A = aderezos (ketchup, mayo, etc.)
    public List<Producto> obtenerAderezosHamburguesa() {
        return productoRepositorio.findByTipoAndActivoTrue("A");
    }
}
