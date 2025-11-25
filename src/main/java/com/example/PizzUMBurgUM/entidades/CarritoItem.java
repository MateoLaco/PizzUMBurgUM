package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "creaciones", "favoritos", "pedidos"})
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_creacion", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "productos", "pedidos", "creador"})
    private Creacion creacion;

    @Column
    @NotNull
    private Integer cantidad;

    @Column
    @NotNull
    private Double precioUnitario;

    @Column
    @NotNull
    private LocalDateTime fechaAgregado;

    @PrePersist
    public void onCreate() {
        this.fechaAgregado = LocalDateTime.now();
    }
}
