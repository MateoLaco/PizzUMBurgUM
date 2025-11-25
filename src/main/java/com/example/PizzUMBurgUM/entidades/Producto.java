package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(length = 50)
    @NotNull
    private String nombre;

    @Column(length = 10)
    @NotNull
    private float precio;

    @Column(length = 1)
    @NotNull
    private String tipo;

    @Column
    @NotNull
    private boolean activo;

    @ManyToMany(mappedBy = "productos")
    private Set<Creacion> creaciones = new HashSet<>();
}