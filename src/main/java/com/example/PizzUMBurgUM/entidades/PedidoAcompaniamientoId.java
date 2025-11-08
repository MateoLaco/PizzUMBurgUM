package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PedidoAcompaniamientoId implements Serializable {
    @Column(name = "id_pedido")
    private Long pedidoId;

    @Column(name = "id_acompaniamiento")
    private Long acompaniamientoId;
}
