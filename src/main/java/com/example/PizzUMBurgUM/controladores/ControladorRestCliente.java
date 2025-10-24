package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ControladorRestCliente {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/")
    public ResponseEntity<List<Cliente>> listarCliente(){
        return ResponseEntity.ok(clienteServicio.obtenerClientes());
    }
}
