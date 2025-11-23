package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Favorito;
import com.example.PizzUMBurgUM.repositorios.FavoritoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoServicio {
    @Autowired
    private FavoritoRepositorio favoritoRepositorio;

    public Favorito guardarFavorito(Favorito favorito) {
        return favoritoRepositorio.save(favorito);
    }

    public List<Favorito> obtenerFavoritosPorCliente(Cliente cliente) {
        return favoritoRepositorio.findByCliente(cliente);
    }

    public List<Favorito> obtenerFavoritosPorClienteYTipo(Cliente cliente, String tipo) {
        return favoritoRepositorio.findByClienteAndTipo(cliente, tipo);
    }

    public boolean eliminarFavorito(Cliente cliente, Long favoritoId) {
        try {
            favoritoRepositorio.deleteByClienteAndId(cliente, favoritoId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean existeFavorito(Cliente cliente, String nombre) {
        return favoritoRepositorio.findByCliente(cliente).stream()
                .anyMatch(f -> f.getNombre().equals(nombre));
    }

    public Optional<Favorito> obtenerFavoritoPorId(Long id) {
        return favoritoRepositorio.findById(id);
    }

    public int contarFavoritosPorCliente(Cliente cliente) {
        return favoritoRepositorio.findByCliente(cliente).size();
    }
}
