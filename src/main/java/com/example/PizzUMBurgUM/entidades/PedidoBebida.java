package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedido_bebida")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoBebida {

    @EmbeddedId
    private PedidoBebidaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY) @MapsId("bebidaId")
    @JoinColumn(name = "id_bebida", nullable = false)
    private Bebida bebida;

    @Column(nullable = false)
    private Integer cantidad;

    // Precio “congelado” en el momento del pedido
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Transient
    public java.math.BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
