package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public Cliente agregarCliente(Cliente unCliente){
        if (unCliente == null){return null;}
        if (clienteRepositorio.existsByEmail(unCliente.getEmail())){return null;}
        clienteRepositorio.save(unCliente);
        return unCliente;           //Crear una forma que utilice EmailDuplicado(excepción)
    }

    public Cliente actualizarCliente(Cliente unCliente){
        if (clienteRepositorio.existsById(unCliente.getId())){
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
}
