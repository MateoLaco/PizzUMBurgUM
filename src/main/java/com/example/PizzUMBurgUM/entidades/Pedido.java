package com.example.PizzUMBurgUM.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(length = 100)
    @NotNull
    @Builder.Default
    private String estado = "EN_COLA";

    @Column
    @Builder.Default
    private Double costoEnvio = 0.0;

    @Column
    @NotNull
    private LocalDate fecha;

    @Column
    @NotNull
    @Builder.Default
    private Double precioTotal = 0.0;

    @ManyToMany
    @JoinTable(
            name = "pedido_creacion",
            joinColumns = @JoinColumn(name = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_creacion")
    )
    @Builder.Default
    private Set<Creacion> creaciones = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "pedido_bebidas", joinColumns = @JoinColumn(name = "id_pedido"))
    @Builder.Default
    private List<PedidoExtraItem> bebidas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "pedido_acompanamientos", joinColumns = @JoinColumn(name = "id_pedido"))
    @Builder.Default
    private List<PedidoExtraItem> acompanamientos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"creaciones", "favoritos", "pedidos"})
    private Cliente cliente;

    @PrePersist
    public void onCreate() {
        this.fecha = LocalDate.now();
    }
}
