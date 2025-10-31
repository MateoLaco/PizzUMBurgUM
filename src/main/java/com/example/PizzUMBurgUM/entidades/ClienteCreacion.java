package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cliente_creacion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_creacion"}))
public class ClienteCreacion {
    @EmbeddedId
    private ClienteCreacionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id_cliente")
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id_creacion")
    @JoinColumn(name = "id_creacion", nullable = false)
    private Creacion creacion;

    @Column
    private boolean favorito;
}
