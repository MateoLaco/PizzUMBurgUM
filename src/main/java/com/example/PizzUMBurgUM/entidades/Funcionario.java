package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "funcionario")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Funcionario extends Usuario {
    @NotNull
    @Column(length = 10, name = "rol")
    private String rol;
}
