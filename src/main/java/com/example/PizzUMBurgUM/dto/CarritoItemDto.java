package com.example.PizzUMBurgUM.dto;

public record CarritoItemDto(
        Long id,
        String nombre,
        String descripcion,
        Double precioUnitario,
        Integer cantidad,
        Double subtotal
) {
}
