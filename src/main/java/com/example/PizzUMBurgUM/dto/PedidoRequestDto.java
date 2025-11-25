package com.example.PizzUMBurgUM.dto;

import java.util.List;

public record PedidoRequestDto(
        Double costoEnvio,
        List<PedidoExtraDto> bebidas,
        List<PedidoExtraDto> acompanamientos
) {
}
