package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.TicketDto;
import com.example.PizzUMBurgUM.servicios.CreacionServicio;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ControladorTicket {
    private final CreacionServicio creacionServicio;

    public ControladorTicket(CreacionServicio creacionServicio) {
        this.creacionServicio = creacionServicio;
    }
    public TicketDto obtenerTicket(@PathVariable Long idCreacion) {
        return creacionServicio.generarTicket(idCreacion);
    }
}
