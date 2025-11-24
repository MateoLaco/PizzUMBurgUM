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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
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
            System.out.println("No se ha podido regostrar el usuario");
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
            List<Favorito> favoritosReales = favoritoServicio.obtenerFavoritosPorCliente(cliente);
            model.addAttribute("favoritos", favoritosReales);
            //historial
            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));
            // Estadísticas
            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));
            model.addAttribute("totalFavoritos", favoritosReales.size());

        } else {
            // Datos de prueba si no hay cliente
            model.addAttribute("hamburguesasRecientes", List.of());
            model.addAttribute("pizzasRecientes", List.of());
            model.addAttribute("favoritos", List.of());
            model.addAttribute("totalHamburguesas", 0);
            model.addAttribute("totalPizzas", 0);
            model.addAttribute("totalFavoritos", 0);
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

        @PostMapping("/guardar-favorito")
        @ResponseBody
        public ResponseEntity<?> guardarFavorito(
                @RequestBody FavoritoDto favoritoDto,
                HttpSession session) {

            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Usuario no autenticado"
                ));
            }

            try {
                // Verificar si ya existe un favorito con el mismo nombre
                if (favoritoServicio.existeFavorito(cliente, favoritoDto.getNombre())) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Ya existe un favorito con ese nombre"
                    ));
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String detallesJson = objectMapper.writeValueAsString(favoritoDto.getDetalle());

                Favorito favorito = Favorito.builder()
                        .nombre(favoritoDto.getNombre())
                        .descripcion(favoritoDto.getDescripcion())
                        .precio(favoritoDto.getPrecio())
                        .tipo(favoritoDto.getTipo().toUpperCase())
                        .detalles(detallesJson)
                        .cliente(cliente)
                        .build();

                Favorito guardado = favoritoServicio.guardarFavorito(favorito);

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Guardado en favoritos correctamente",
                        "favoritoId", guardado.getId()
                ));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Error al guardar en favoritos: " + e.getMessage()
                ));
            }
        }

        @GetMapping("/favoritos")
        public String verFavoritos(Model model, HttpSession session) {
            if (!verificarSesion(session)) return "redirect:/auth/login";

            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            List<Favorito> favoritos = favoritoServicio.obtenerFavoritosPorCliente(cliente);

            // Separar por tipo para la vista
            List<Favorito> pizzasFavoritas = favoritos.stream()
                    .filter(f -> "PIZZA".equals(f.getTipo()))
                    .collect(Collectors.toList());

            List<Favorito> hamburguesasFavoritas = favoritos.stream()
                    .filter(f -> "HAMBURGUESA".equals(f.getTipo()))
                    .collect(Collectors.toList());

            // AGREGAR CONTADORES AL MODELO
            model.addAttribute("favoritos", favoritos);
            model.addAttribute("favoritosPizza", pizzasFavoritas); //
            model.addAttribute("favoritosHamburguesa", hamburguesasFavoritas); //
            model.addAttribute("totalFavoritos", favoritos.size()); // ← CONTADOR TOTAL
            model.addAttribute("cliente", cliente);

            return "cliente/favoritos";
        }

        @PostMapping("/favorito/{id}/pedir")
        @ResponseBody
        public ResponseEntity<?> pedirFavorito(@PathVariable Long id, HttpSession session) {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "No autenticado"));
            }

            try {
                Optional<Favorito> favoritoOpt = favoritoServicio.obtenerFavoritoPorId(id);
                if (favoritoOpt.isEmpty() || !favoritoOpt.get().getCliente().getIdUsuario().equals(cliente.getIdUsuario())) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Favorito no encontrado"));
                }

                Favorito favorito = favoritoOpt.get();

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", favorito.getNombre() + " agregado al carrito",
                        "precio", favorito.getPrecio()
                ));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Error al procesar el pedido"
                ));
            }
        }

        @DeleteMapping("/favorito/{id}")
        @ResponseBody
        public ResponseEntity<?> eliminarFavorito(@PathVariable Long id, HttpSession session) {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401).body(Map.of("success", false, "message", "No autenticado"));
            }

            boolean eliminado = favoritoServicio.eliminarFavorito(cliente, id);
            if (eliminado) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Favorito eliminado"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Error al eliminar"));
            }
        }


    @GetMapping("/ordenes")
    public String ordenes(HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        return "cliente/ordenes";
    }

    @Controller
    @RequestMapping("/cliente")
    public class ClienteController {

        @GetMapping("/carrito")
        public String carrito() {
            return "cliente/carrito";
        }
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
            List<Favorito> favoritos = favoritoServicio.obtenerFavoritosPorCliente(cliente);
            model.addAttribute("favoritos", favoritos);
            model.addAttribute("totalFavoritos", favoritos.size());

            // Historial
            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));

            // Estadísticas
            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));
            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
        } else {
            // Valores por defecto si no hay cliente
            model.addAttribute("favoritos", java.util.Collections.emptyList());
            model.addAttribute("totalFavoritos", 0);
            model.addAttribute("totalPizzas", 0);
            model.addAttribute("totalHamburguesas", 0);
            model.addAttribute("historialPizzas", java.util.Collections.emptyList());
            model.addAttribute("historialHamburguesas", java.util.Collections.emptyList());
        }

        return "cliente/perfil";
    }

}
