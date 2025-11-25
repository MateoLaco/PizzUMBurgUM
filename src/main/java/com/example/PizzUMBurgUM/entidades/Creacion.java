package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "creacion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Creacion {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_creacion;

    @NotNull
    @Column(length = 1)
    private char tipo; //pizza o hamburguesa

    @Column(name = "es_favorito")
    @Builder.Default
    private boolean favorito = false;

    @ManyToMany
    @JoinTable(
            name = "creacion_producto",
            joinColumns = @JoinColumn(name = "idCreacion"),
            inverseJoinColumns = @JoinColumn(name = "idProducto")
    )
    @JsonIgnoreProperties({"creaciones"})
    @Builder.Default
    private Set<Producto> productos = new HashSet<>();

    @ManyToMany(mappedBy = "creaciones")
    @JsonIgnoreProperties({"creaciones"})
    @Builder.Default
    private Set<Pedido> pedidos = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"creaciones", "favoritos", "pedidos"})
    private Cliente creador;

    private String nombre;
    private String descripcion;
    private Double precio;
    private LocalDate fechaCreacion;
}