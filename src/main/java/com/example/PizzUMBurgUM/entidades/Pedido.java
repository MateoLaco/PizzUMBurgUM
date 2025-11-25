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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(length = 100)
    @NotNull
    private String estado;

    @Column
    @NotNull
    private String acompanamiento;

    @Column
    @NotNull
    private String bebida;

    @Column
    private Integer costoEnvio;

    @Column
    @NotNull
    private LocalDate fecha;

    @Column
    private Integer precioTotal;

    @ManyToMany
    @JoinTable(
            name = "pedido_creacion",
            joinColumns = @JoinColumn(name = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_creacion")
    )
    private Set<Creacion> creaciones = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.fecha = LocalDate.now();
    }
}
