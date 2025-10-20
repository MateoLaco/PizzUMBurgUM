package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public Producto agregarProducto(Producto unProducto) {return unProducto == null ? null: productoRepositorio.save(unProducto);}

    public Producto actualizarProducto(Producto unProducto) {
        if(productoRepositorio.existsById(unProducto.getIdProducto())){
            return productoRepositorio.save(unProducto);
        }
        return null;
    }

    public boolean eliminarProducto(Integer idProducto) {
        if(idProducto!=null){
            productoRepositorio.deleteById(idProducto);
            return true;
        }
        return false;
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepositorio.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Integer idProducto) {
        return productoRepositorio.findById(idProducto);
    }
}
