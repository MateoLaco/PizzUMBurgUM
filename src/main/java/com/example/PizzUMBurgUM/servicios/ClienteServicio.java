package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Cliente agregarCliente(String nombreUsuario,
                                  String email,
                                  String contrasena,
                                  String metodoPago,
                                  LocalDate fechaNacimiento,
                                  String direccion,
                                  String telefono,
                                  String numeroTarjeta) {

        // Validaciones básicas
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new RuntimeException("El nombre de usuario no puede estar vacío");
        }
        if (email == null || email.isBlank()) {
            throw new RuntimeException("El email no puede estar vacío");
        }
        if (clienteRepositorio.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }
        if (metodoPago == null || metodoPago.isBlank()) {
            throw new RuntimeException("Debe especificarse un método de pago");
        }
        if (fechaNacimiento == null) {
            throw new RuntimeException("Debe indicar la fecha de nacimiento");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new RuntimeException("La dirección no puede estar vacía");
        }
        if (telefono == null || telefono.length() < 8 || telefono.length() > 13) {
            throw new RuntimeException("El teléfono debe tener entre 8 y 13 dígitos");
        }

        Cliente nuevoCliente = Cliente.builder()
                .nombreUsuario(nombreUsuario)
                .email(email)
                .contrasena(contrasena)
                .metodoPago(metodoPago)
                .fechaNacimiento(fechaNacimiento)
                .direccion(direccion)
                .telefono(telefono)
                .numeroTarjeta(numeroTarjeta)
                .build();

        clienteRepositorio.save(nuevoCliente);
        return nuevoCliente;
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
    public List<Creacion> obtenerHamburguesasRecientes(Cliente cliente) {
        return creacionRepositorio.findByCreadorAndTipo(cliente, 'H');
    }
    // Obtener pizzas creadas recientemente
    public List<Creacion> obtenerPizzasRecientes(Cliente cliente) {
        return creacionRepositorio.findByCreadorAndTipo(cliente, 'P');
    }

    // Obtener favoritos
    public List<Creacion> obtenerFavoritos(Cliente cliente, boolean favorito) {
        if (cliente == null) return List.of();
        return creacionRepositorio.findByCreadorAndFavorito(cliente, favorito);
    }

    // Contar para estadísticas (opcional)
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

