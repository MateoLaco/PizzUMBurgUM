package com.example.PizzUMBurgUM.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tarjetas")
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String vencimiento;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private String tipo; // "credito" o "debito"

    @Column(nullable = false)
    private String marca; // "Visa", "MasterCard", "American Express"

    @Column(nullable = false)
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}