package com.example.PizzUMBurgUM.repositorios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepositorio extends JpaRepository<Favorito, Long> {
    List<Favorito> findByCliente(Cliente cliente);
    List<Favorito> findByClienteAndTipo(Cliente cliente, String tipo);
    void deleteByClienteAndId(Cliente cliente, Long id);
}
