package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.dto.DetalleDto;
import com.example.PizzUMBurgUM.dto.TicketDto;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
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

    public TicketDto generarTicket(Integer idCreacion) {
        Creacion creacion = creacionRepositorio.findById(idCreacion).orElseThrow();

        List<DetalleDto> detalle = creacion.getProductos().stream()
                .map(p -> new DetalleDto(p.getNombre(), (int) p.getPrecio()))
                .toList();

        int total = detalle.stream()
                .mapToInt(DetalleDto::costo)
                .sum();

        return new TicketDto(
                creacion.getIdCreacion(),
                creacion.getCreador().getEmail(),
                detalle,
                total
        );
    }

}