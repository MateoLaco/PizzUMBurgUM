package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Direccion;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.repositorios.DireccionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DireccionServicio {

    @Autowired
    private DireccionRepositorio direccionRepositorio;

    public List<Direccion> obtenerDireccionesPorCliente(Cliente cliente) {
        return direccionRepositorio.findByCliente(cliente);
    }

    public Direccion guardarDireccion(Direccion direccion) {
        return direccionRepositorio.save(direccion);
    }

    public void eliminarDireccion(Cliente cliente, Long id) {
        direccionRepositorio.deleteByClienteAndId(cliente, id);
    }

    public Optional<Direccion> obtenerDireccionPorIdYCliente(Long id, Cliente cliente) {
        return direccionRepositorio.findById(id)
                .filter(direccion -> direccion.getCliente().getIdUsuario().equals(cliente.getIdUsuario()));
    }
}