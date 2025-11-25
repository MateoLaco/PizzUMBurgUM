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
    // EN DireccionServicio - AGREGAR estos métodos:

    public boolean existeDireccionConNombre(Cliente cliente, String nombre) {
        return direccionRepositorio.existsByClienteAndNombreIgnoreCase(cliente, nombre);
    }
    public void quitarPrincipalDeTodas(Cliente cliente) {
        List<Direccion> direcciones = direccionRepositorio.findByCliente(cliente);
        for (Direccion dir : direcciones) {
            dir.setPrincipal(false);
        }
        direccionRepositorio.saveAll(direcciones);
    }

    public void marcarComoPrincipal(Cliente cliente, Long direccionId) {
        // Quitar principal de todas
        quitarPrincipalDeTodas(cliente);

        // Marcar la nueva como principal
        Direccion direccion = direccionRepositorio.findById(direccionId)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        if (!direccion.getCliente().getIdUsuario().equals(cliente.getIdUsuario())) {
            throw new RuntimeException("La dirección no pertenece al cliente");
        }

        direccion.setPrincipal(true);
        direccionRepositorio.save(direccion);
    }
}