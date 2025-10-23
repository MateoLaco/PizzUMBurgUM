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

    @PostMapping("/nuevo")
    public ResponseEntity<Cliente> nuevoCliente(@RequestParam String nombreUsuario, @RequestParam String contrasena, @RequestParam String tel, @RequestParam String email) {
        Cliente clienteNuevo = Cliente.builder()
                .nombreCompleto(nombreUsuario)
                .contrasena(contrasena)
                .telefono(tel)
                .email(email)
                .build();
        clienteServicio.agregarCliente(clienteNuevo);
        return ResponseEntity.ok(clienteNuevo);
    }
}
