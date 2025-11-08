// Pago.java
package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "pago")
public class Pago {

    // PK compartida con Pedido (weak entity)
    @Id
    @Column(name = "id_pedido")
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Se calcula/reflecta desde Pedido (total del pedido)
    @NotNull
    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;
}
