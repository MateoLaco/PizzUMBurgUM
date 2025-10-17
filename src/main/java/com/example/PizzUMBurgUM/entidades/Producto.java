package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(length = 100)
    private int idProducto;

    @Column(length = 50)
    @NotNull
    private String nombre;

    @Column(length = 10)
    @NotNull
    private float precio;

    @Column(length = 10)
    @NotNull
    private String tipo;

    @ManyToMany(mappedBy = "productos")
    private Set<Creacion> creaciones = new HashSet<>();
}