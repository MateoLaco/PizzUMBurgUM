package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreacionPizzaServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public List<Producto> obtenerMasas() {
        return productoRepositorio.findByTipoAndActivoTrue("M");
    }

    public List<Producto> obtenerSalsas() {
        return productoRepositorio.findByTipoAndActivoTrue("S");
    }

    public List<Producto> obtenerQuesosPizza() {
        // En admin se crea como tipo "QP" (Queso de Pizza)
        return productoRepositorio.findByTipoAndActivoTrue("QP");
    }

    public List<Producto> obtenerToppingsGenerales() {
        return productoRepositorio.findByTipoAndActivoTrue("P");
    }
}
