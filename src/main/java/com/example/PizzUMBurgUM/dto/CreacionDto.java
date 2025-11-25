package com.example.PizzUMBurgUM.dto;

import java.util.List;

public record CreacionDto(
        String nombre,
        String descripcion,
        Double precio,
        char tipo,
        List<Long> productosIds,
        boolean favorito
) {
}
