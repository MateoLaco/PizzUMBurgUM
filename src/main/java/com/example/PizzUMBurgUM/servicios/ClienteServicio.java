package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private CreacionRepositorio creacionRepositorio;


    public Cliente agregarCliente(Cliente unCliente){
        if (unCliente == null){
            throw new RuntimeException("El cliente no puede ser null");}
        if (clienteRepositorio.existsByEmail(unCliente.getEmail())){
            throw new RuntimeException("El email ya está registrado");}
        clienteRepositorio.save(unCliente);
        return unCliente;
    }


    public Cliente actualizarCliente(Cliente unCliente){
        if (clienteRepositorio.existsById(unCliente.getIdUsuario())){
            return clienteRepositorio.save(unCliente);
        }
        return null;
    }

    public boolean eliminarCliente(Long idCliente){
        if (idCliente != null){
            clienteRepositorio.deleteById(idCliente);
            return true;
        }
        return false;
    }

    public List<Cliente> obtenerClientes(){return clienteRepositorio.findAll();}
    public Cliente findByEmail(String email) {
        return clienteRepositorio.findByEmail(email);
    }
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    public Cliente actualizarNombre(Cliente cliente, String nuevoNombre) {
        cliente.setNombreUsuario(nuevoNombre);
        return clienteRepositorio.save(cliente);
    }

    public Cliente actualizarPassword(Cliente cliente, String nuevaPassword) {
        cliente.setContrasena(nuevaPassword);
        return clienteRepositorio.save(cliente);
    }

    public Cliente actualizarMetodoPago(Cliente cliente, String metodoPago, String numero, String nombreTarjeta, String vencimiento, String cvv) {
        cliente.setMetodoPago(metodoPago);
        cliente.setNumeroTarjeta(numero);
        cliente.setNombreTarjeta(nombreTarjeta);
        cliente.setVencimientoTarjeta(vencimiento);
        cliente.setCvvTarjeta(cvv);
        return clienteRepositorio.save(cliente);
    }
    public List<Creacion> obtenerHamburguesasRecientes(Cliente cliente) {
        return creacionRepositorio.findByCreadorAndTipo(cliente, 'H');
    }

    public List<Creacion> obtenerPizzasRecientes(Cliente cliente) {
        return creacionRepositorio.findByCreadorAndTipo(cliente, 'P');
    }

    public int contarTotalHamburguesas(Cliente cliente) {
        if (cliente == null) return 0;
        List<Creacion> hamburguesas = creacionRepositorio.findByCreadorAndTipo(cliente, 'H');
        return hamburguesas.size();
    }

    public int contarTotalPizzas(Cliente cliente) {
        if (cliente == null) return 0;
        List<Creacion> pizzas = creacionRepositorio.findByCreadorAndTipo(cliente, 'P');
        return pizzas.size();
    }
}

