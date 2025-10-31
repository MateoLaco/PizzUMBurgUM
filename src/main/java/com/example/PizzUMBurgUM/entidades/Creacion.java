package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
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
@Table(name = "creacion")
public class Creacion {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCreacion;

    @NotNull
    @Column(length = 1)
    private char tipo; //pizza o hamburguesa

    @Column(name = "es_favorito")
    private boolean favorito = false;

    @ManyToMany
    @JoinTable(
            name = "creacion_producto",
            joinColumns = @JoinColumn(name = "idCreacion"),
            inverseJoinColumns = @JoinColumn(name = "idProducto")
    )
    private Set<Producto> productos = new HashSet<>();

    @ManyToMany(mappedBy = "creaciones")
    private Set<Pedido> pedidos = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente creador;

    @OneToMany(mappedBy = "creacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClienteCreacion> relacionesConClientes = new HashSet<>();
}