package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}
