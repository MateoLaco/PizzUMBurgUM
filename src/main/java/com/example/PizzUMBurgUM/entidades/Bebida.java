package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "bebida")
public class Bebida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bebida")
    private Long id_bebida;

    @NotNull
    @Column(name = "nombre_bebida")
    private String nombre_bebida;

    @NotNull
    @Column(name = "precio_bebida")
    private BigDecimal precio_bebida;

    @OneToMany(mappedBy = "bebida")
    private Set<PedidoBebida> pedidos;

    @PrePersist
    public void onCreate() {
        pedidos = new HashSet<>();}
}
