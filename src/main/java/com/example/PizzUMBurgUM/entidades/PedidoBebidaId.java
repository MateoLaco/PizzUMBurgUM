package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class PedidoBebidaId implements Serializable {
    @Column(name = "id_pedido") private Long pedidoId;
    @Column(name = "id_bebida") private Long bebidaId;
}
