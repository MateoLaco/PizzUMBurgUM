package com.example.PizzUMBurgUM.entidades;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClienteCreacionId implements Serializable {
    private Long id_cliente;
    private Long id_creacion;
}
