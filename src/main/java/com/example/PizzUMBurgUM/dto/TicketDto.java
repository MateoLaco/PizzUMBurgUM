package com.example.PizzUMBurgUM.dto;
import java.util.List;

public record TicketDto(
        Long idCreacion,
        String clienteEmail,
        List<DetalleDto> detalle,
        int total
) {
}
