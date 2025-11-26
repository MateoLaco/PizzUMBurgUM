package com.example.PizzUMBurgUM.dto;

import java.time.LocalDate;
import java.util.List;

public record TicketVentaDto(
        Long pedidoId,
        LocalDate fecha,
        String estado,
        String clienteEmail,
        Double total,
        Double costoEnvio,
        int cantidadCreaciones,
        List<String> bebidas,
        List<String> acompanamientos
) {
}
