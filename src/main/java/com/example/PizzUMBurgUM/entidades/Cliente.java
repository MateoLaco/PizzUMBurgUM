package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private Long idCliente;

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

    @NotNull
    @Column
    private String metodoPago;

    @NotNull
    @Column
    private LocalDate fechaRegistro;

    @NotNull
    @Column(length = 50)
    private String direccion;

    @NotNull
    @Column
    private LocalDate fechaNacimiento;

    @NotNull
    @Column(length = 9)
    @Size(min = 8, max = 13)
    private String telefono;

    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL)
    private List<Creacion> creaciones;

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }
}
