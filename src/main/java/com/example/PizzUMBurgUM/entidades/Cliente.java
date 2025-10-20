package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table()
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email(message = "El email no contiene el formato indicado. Ej: [usuario@dominio.com]")
    @Column(length = 50, unique = true)
    private String email;

    @NotNull
    @Column
    private Date fechaRegistro;

    @NotBlank
    @Column
    private String contrasena;

    @NotBlank
    @Column(length = 50)
    private String nombreCompleto;

    @NotNull
    @Column(length = 9)
    private int telefono;

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = new Date();
    }
}
