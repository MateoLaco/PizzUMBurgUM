package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Tarjeta;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.repositorios.TarjetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TarjetaServicio {

    @Autowired
    private TarjetaRepositorio tarjetaRepositorio;

    public List<Tarjeta> obtenerTarjetasPorCliente(Cliente cliente) {
        return tarjetaRepositorio.findByCliente(cliente);
    }

    public Tarjeta guardarTarjeta(Tarjeta tarjeta) {
        return tarjetaRepositorio.save(tarjeta);
    }

    public void eliminarTarjeta(Cliente cliente, Long id) {
        tarjetaRepositorio.deleteByClienteAndId(cliente, id);
    }

    public Optional<Tarjeta> obtenerTarjetaPorIdYCliente(Long id, Cliente cliente) {
        return tarjetaRepositorio.findById(id)
                .filter(tarjeta -> tarjeta.getCliente().getIdUsuario().equals(cliente.getIdUsuario()));
    }
}