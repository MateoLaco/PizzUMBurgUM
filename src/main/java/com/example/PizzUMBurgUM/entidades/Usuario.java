package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank
    @Column(length = 50)
    private String nombreUsuario;

    @NotBlank
    @Email(message = "El email no contiene el formato indicado. Ej: [usuario@dominio.com]")
    @Column(length = 50, unique = true)
    private String email;

    @NotBlank
    @Column
    private String contrasena;

}
