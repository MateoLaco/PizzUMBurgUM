package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pedido")
public class Pedido {

    // Identificador del pedido, lo genera solo H2
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    // El estado en el que se encuentra el pedido, es decir, "en cola", "en preparación", "en camino" o "entregado"
    @Column(length = 100, name = "estado")
    @NotNull
    private String estado;

    // Una tabla de todos los acompañamientos que tiene el pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoAcompaniamiento> acompaniamientos = new HashSet<>();

    // Una tabla de todas las bebidas que tiene el pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoBebida> bebidas = new HashSet<>();

    // El costo del delivery
    @Column(name = "costo_envio")
    private Integer costoEnvio;

    // Fecha en la que se efectuó el pedido
    @Column(name = "fecha")
    @NotNull
    private LocalDate fecha;

    // Todas las creaciones que tiene el pedido
    @ManyToMany
    @JoinTable(
            name = "pedido_creacion",
            joinColumns = @JoinColumn(name = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_creacion")
    )
    private Set<Creacion> creaciones = new HashSet<>();

    // Id del cliente dueño del pedido, sirve para obtener el domicilio además de guardar la información del dueño de dicho pedido
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @PrePersist
    public void onCreate() {
        this.fecha = LocalDate.now();
    }
}
