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
    @Column(name = "id_producto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(length = 50, name = "nombre")
    @NotNull
    private String nombre;

    @Column(length = 10, name = "precio")
    @NotNull
    private float precio;

    @Column(length = 1, name = "tipo")
    @NotNull
    private char tipo;

    @ManyToMany(mappedBy = "productos")
    private Set<Creacion> creaciones = new HashSet<>();
}