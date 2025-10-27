package com.example.PizzUMBurgUM.dto;
import java.util.List;

public record TicketDto(
        Integer idCreacion,
        String clienteEmail,
        List<DetalleDto> detalle,
        int total
) {
}
