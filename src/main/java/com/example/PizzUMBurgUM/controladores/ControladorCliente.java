package com.example.PizzUMBurgUM.controladores;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ClienteServicio clienteServicio;

    // FILTRO MANUAL PARA VERIFICAR SESIÓN
    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("clienteLogueado") != null;
    }

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
            return "redirect:/auth/register";
        }
        else  {
            return "redirect:/inicio";
        }
    }
    @GetMapping("/panel")
    public String panelPrincipal(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/auth/login";
        }
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        if (cliente != null) {
            // Obtener las creaciones reales para mostrar
            model.addAttribute("hamburguesasRecientes", clienteServicio.obtenerHamburguesasRecientes(cliente));
            model.addAttribute("pizzasRecientes", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("favoritos", clienteServicio.obtenerFavoritos(cliente, true));
            //historial
            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));

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

        model.addAttribute("userEmail", cliente.getEmail());
        model.addAttribute("userName", cliente.getNombreUsuario());

        return "cliente/panel";
    }



    @GetMapping("/crear-hamburguesa")
    public String crearHamburguesa(HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        return "cliente/crearHamburguesa";
    }

    @GetMapping("/crear-pizza")
    public String crearPizza(HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        return "cliente/crearPizza";
    }

    @GetMapping("/favoritos")
    public String favoritos(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (!verificarSesion(session)) return "redirect:/auth/login";;
        if (cliente != null) {
            model.addAttribute("favoritos", clienteServicio.obtenerFavoritos(cliente, true));
        }
        return "cliente/favoritos";
    }

    @GetMapping("/ordenes")
    public String ordenes(HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        return "cliente/ordenes";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/auth/login";
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        if (cliente != null) {
            // Datos del cliente
            model.addAttribute("cliente", cliente);

            // Favoritos
            model.addAttribute("favoritos", clienteServicio.obtenerFavoritos(cliente, true));

            // Historial (puedes usar las creaciones como historial)
            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));

            // Estadísticas
            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));
            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
            model.addAttribute("totalFavoritos", clienteServicio.obtenerFavoritos(cliente, true).size());
        }

        return "cliente/perfil";
    }
}
