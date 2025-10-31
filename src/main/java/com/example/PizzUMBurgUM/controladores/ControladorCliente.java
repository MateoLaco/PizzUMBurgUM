package com.example.PizzUMBurgUM.controladores;

import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Usuario;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ClienteServicio clienteServicio;

    @PostMapping("/nuevo")
    public String nuevoCliente(
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String metodoPago,
            @RequestParam String direccion,
            @RequestParam int diaNacimiento,
            @RequestParam int mesNacimiento,
            @RequestParam int anioNacimiento,
            @RequestParam String codigoPais,
            @RequestParam String tel
    ) {
        LocalDate fechaNacimiento = LocalDate.of(anioNacimiento, mesNacimiento, diaNacimiento);
        String telefono = codigoPais + tel;
        Cliente clienteNuevo = Cliente.builder()
                .nombreUsuario(nombreUsuario)
                .email(email)
                .contrasena(contrasena)
                .metodoPago(metodoPago)
                .direccion(direccion)
                .fechaNacimiento(fechaNacimiento)
                .telefono(telefono)
                .build();
        Cliente a = clienteServicio.agregarCliente(clienteNuevo);
        if (a == null) {
            System.out.println("No se ha podido regostrar el usuario");
            return "redirect:/auth/register";
        }
        else  {
            return "redirect:/inicio";
        }
    }
    @GetMapping("/panel")
    public String panelPrincipal(Model model, Principal principal) {
        String email = principal.getName();
        Cliente cliente = clienteServicio.findByEmail(email);

        if (cliente != null) {
            // Obtener las creaciones reales para mostrar
            model.addAttribute("hamburguesasRecientes", clienteServicio.obtenerHamburguesasRecientes(cliente));
            model.addAttribute("pizzasRecientes", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("favoritos", clienteServicio.obtenerFavoritos(cliente, true));

            // Estadísticas
            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));

        } else {
            // Datos de prueba si no hay cliente
            model.addAttribute("hamburguesasRecientes", List.of());
            model.addAttribute("pizzasRecientes", List.of());
            model.addAttribute("favoritos", List.of());
            model.addAttribute("totalHamburguesas", 0);
            model.addAttribute("totalPizzas", 0);
        }

        model.addAttribute("userEmail", email);
        model.addAttribute("userName", email.split("@")[0]);

        return "cliente/panel";
    }



    @GetMapping("/crear-hamburguesa")
    public String crearHamburguesa() {
        return "cliente/crearHamburguesa";
    }

    @GetMapping("/crear-pizza")
    public String crearPizza() {
        return "cliente/crearPizza";
    }

    @GetMapping("/favoritos")
    public String favoritos(Model model, Principal principal) {
        String email = principal.getName();
        Cliente cliente = clienteServicio.findByEmail(email);
        if (cliente != null) {
            model.addAttribute("favoritos", clienteServicio.obtenerFavoritos(cliente, true));
        }
        return "cliente/favoritos";
    }

    @GetMapping("/ordenes")
    public String ordenes() {
        return "cliente/ordenes";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "cliente/perfil";
    }

}
