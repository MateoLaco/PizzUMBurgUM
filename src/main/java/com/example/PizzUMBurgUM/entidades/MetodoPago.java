package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "metodo_pago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // Visa / MasterCard / etc.

    @Column(nullable = false)
    private String numero; // sin espacios

    @Column(nullable = false)
    private String nombreTitular;

    @Column(nullable = false)
    private String vencimiento; // MM/YYYY

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    @Builder.Default
    private boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    @PrePersist
    public void onCreate() {
        this.fechaAlta = LocalDateTime.now();
    }
}
