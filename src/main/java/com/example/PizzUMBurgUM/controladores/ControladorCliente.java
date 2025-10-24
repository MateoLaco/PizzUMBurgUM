package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ClienteServicio clienteServicio;

    // Crear una forma para que si está loggeado, me mande a otra página. Probablemente usar otro archivo html

    @PostMapping("/nuevo")
    public String nuevoCliente(@RequestParam String nombreUsuario, @RequestParam String contrasena, @RequestParam String tel, @RequestParam String email) {
        Cliente clienteNuevo = Cliente.builder()
                .nombreCompleto(nombreUsuario)
                .contrasena(contrasena)
                .telefono(tel)
                .email(email)
                .build();
        Cliente a = clienteServicio.agregarCliente(clienteNuevo);
        if (a == null) {
            return "redirect:/auth/register";
        }
        else  {
            return "redirect:/inicio";
        }
    }
}
