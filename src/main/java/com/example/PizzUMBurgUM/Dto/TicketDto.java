package com.example.PizzUMBurgUM.Dto;
import java.util.List;

public record TicketDto(
        Long idCreacion,
        String clienteEmail,
        List<DetalleDto> detalle,
        int total
) {
}
