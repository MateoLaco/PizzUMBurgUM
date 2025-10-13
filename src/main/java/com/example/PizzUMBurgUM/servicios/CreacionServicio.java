package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreacionServicio {

    @Autowired
    private CreacionRepositorio creacionRepositorio;

    public Creacion agregarCreacion(Creacion unaCreacion){return unaCreacion == null ? null: creacionRepositorio.save(unaCreacion);}

    public Creacion actualizarCreacion(Creacion unaCreacion){
        if (creacionRepositorio.existsById(unaCreacion.getIdCreacion())) {
            return creacionRepositorio.save(unaCreacion);
        }
        return null;
    }

    public boolean eliminarCreacion(Integer idCreacion){
        if (idCreacion != null) {
            creacionRepositorio.deleteById(idCreacion);
            return true;
        }
        return false;
    }

    public List<Creacion> obtenerCreaciones(){return creacionRepositorio.findAll();}
}