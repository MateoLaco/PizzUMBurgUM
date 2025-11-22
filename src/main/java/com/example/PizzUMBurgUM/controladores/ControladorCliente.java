package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.FavoritoDto;
import com.example.PizzUMBurgUM.entidades.Favorito;
import com.example.PizzUMBurgUM.servicios.FavoritoServicio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ClienteServicio clienteServicio;
    private FavoritoServicio favoritoServicio;

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

        if (cliente != null) {
            try {
                List<Favorito> favoritos = favoritoServicio.obtenerFavoritosPorCliente(cliente);

                // Separar por tipo para las estadísticas
                List<Favorito> favoritosPizza = favoritos.stream()
                        .filter(f -> "PIZZA".equals(f.getTipo()))
                        .collect(Collectors.toList());

                List<Favorito> favoritosHamburguesa = favoritos.stream()
                        .filter(f -> "HAMBURGUESA".equals(f.getTipo()))
                        .collect(Collectors.toList());

                model.addAttribute("favoritos", favoritos);
                model.addAttribute("favoritosPizza", favoritosPizza);
                model.addAttribute("favoritosHamburguesa", favoritosHamburguesa);
                model.addAttribute("totalFavoritos", favoritos.size());

            } catch (Exception e) {
                model.addAttribute("favoritos", new ArrayList<>());
                model.addAttribute("favoritosPizza", new ArrayList<>());
                model.addAttribute("favoritosHamburguesa", new ArrayList<>());
                model.addAttribute("totalFavoritos", 0);
            }
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
            try {
                List<Favorito> favoritos = favoritoServicio.obtenerFavoritosPorCliente(cliente);
                model.addAttribute("favoritos", favoritos);
                model.addAttribute("totalFavoritos", favoritos.size());
            } catch (Exception e) {
                model.addAttribute("favoritos", java.util.Collections.emptyList());
                model.addAttribute("totalFavoritos", 0);
            }

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
    // MÉTODOS PARA API DE FAVORITOS (desde ControladorFavorito)
    @PostMapping("/guardar-favorito")
    @ResponseBody
    public ResponseEntity<?> guardarFavorito(
            @RequestBody FavoritoDto favoritoDto,
            HttpSession session) {

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Usuario no autenticado");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String detalles = objectMapper.writeValueAsString(favoritoDto);

            Favorito favorito = Favorito.builder()
                    .nombre(favoritoDto.getNombre())
                    .descripcion(favoritoDto.getDescripcion())
                    .precio(favoritoDto.getPrecio())
                    .tipo("HAMBURGUESA")
                    .detalles(detalles)
                    .cliente(cliente)
                    .build();

            Favorito guardado = favoritoServicio.guardarFavorito(favorito);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hamburguesa guardada en favoritos");
            response.put("favoritoId", guardado.getId());

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al procesar los datos");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/eliminar-favorito/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long id, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Usuario no autenticado");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        boolean eliminado = favoritoServicio.eliminarFavorito(cliente, id);
        Map<String, Object> response = new HashMap<>();
        if (eliminado) {
            response.put("success", true);
            response.put("message", "Favorito eliminado");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al eliminar favorito");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
