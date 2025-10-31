package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
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

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClienteCreacion> clienteCreaciones = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }
}
