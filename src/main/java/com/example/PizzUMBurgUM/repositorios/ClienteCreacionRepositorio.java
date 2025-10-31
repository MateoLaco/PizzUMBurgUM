package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.ClienteCreacion;
import com.example.PizzUMBurgUM.entidades.ClienteCreacionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteCreacionRepositorio extends JpaRepository<ClienteCreacion, ClienteCreacionId> {
}
