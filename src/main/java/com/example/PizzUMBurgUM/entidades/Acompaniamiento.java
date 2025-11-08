package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "acompaniamiento")
public class Acompaniamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acompaniamiento")
    private Long id_acompaniamiento;

    @Column(nullable = false, length = 100)
    private String nombre_acompaniamiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_acompaniamiento;

    @OneToMany(mappedBy = "acompaniamiento")
    private Set<PedidoAcompaniamiento> pedidos = new HashSet<>();
}
