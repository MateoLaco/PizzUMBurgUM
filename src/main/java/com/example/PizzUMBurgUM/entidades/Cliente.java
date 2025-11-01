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
@Table(name = "CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @NotBlank
    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @NotBlank
    @Email(message = "El email no contiene el formato indicado. Ej: [usuario@dominio.com]")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column
    private String contrasena;

    @NotNull
    @Column(name = "metodo pago")
    private String metodoPago;

    @NotNull
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @NotNull
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotNull
    @Column(name = "direccion")
    private String direccion;


    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;

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
