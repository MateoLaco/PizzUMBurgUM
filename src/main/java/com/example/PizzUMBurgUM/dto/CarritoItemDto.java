package com.example.PizzUMBurgUM.dto;

public record CarritoItemDto(
        Long id,
        Long creacionId,
        String nombre,
        String descripcion,
        char tipo,
        Double precioUnitario,
        Integer cantidad
) {}
