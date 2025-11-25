package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private String nombre; // Ej: "Casa", "Trabajo", "Departamento"

    @Column(nullable = false)
    private String calle;

//    private String referencias;

    @Column(nullable = false)
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}