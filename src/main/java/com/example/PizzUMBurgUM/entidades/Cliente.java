package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Cliente extends Usuario{

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
    @Column(length = 9, name = "telefono")
    @Size(min = 8, max = 13)
    private String telefono;

    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL)
    private List<Creacion> creaciones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }
}
