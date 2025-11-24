package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(name = "metodo_pago")
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

    @Column(name = "nombre_tarjeta")
    private String nombreTarjeta;

    @Column(name = "vencimiento_tarjeta")
    private String vencimientoTarjeta;

    @NotNull
    @Column(length = 15)
    @Size(min = 8, max = 15)
    private String telefono;

    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL)
    private List<Creacion> creaciones;

    // NUEVAS RELACIONES
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarjeta> tarjetas = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }

    // Métodos helper para agregar direcciones/tarjetas
    public void agregarDireccion(Direccion direccion) {
        direcciones.add(direccion);
        direccion.setCliente(this);
    }

    public void agregarTarjeta(Tarjeta tarjeta) {
        tarjetas.add(tarjeta);
        tarjeta.setCliente(this);
    }
}
