package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoExtraItem {

    @Column(name = "id_producto")
    private Long productoId;

    private String nombre;

    private String tipo;

    @Builder.Default
    private Integer cantidad = 1;

    @Builder.Default
    private Double precioUnitario = 0.0;
}
