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
@Table(name = "creacion")
public class Creacion {

    @Id
    @NotNull
    @Column(length = 10)
    private int idCreacion;

    @ManyToMany
    @JoinTable(
            name = "creacion_producto",
            joinColumns = @JoinColumn(name = "idCreacion"),
            inverseJoinColumns = @JoinColumn(name = "idProducto")
    )
    private Set<Producto> productos = new HashSet<>();
}
