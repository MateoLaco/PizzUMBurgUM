package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.FavoritoDto;
import com.example.PizzUMBurgUM.dto.CreacionDto;
import com.example.PizzUMBurgUM.dto.CarritoItemDto;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.entidades.CarritoItem;
import com.example.PizzUMBurgUM.entidades.Favorito;
import com.example.PizzUMBurgUM.servicios.CreacionHamburguesaServicio;
import com.example.PizzUMBurgUM.servicios.FavoritoServicio;
import com.example.PizzUMBurgUM.servicios.CreacionServicio;
import com.example.PizzUMBurgUM.servicios.ProductoServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import com.example.PizzUMBurgUM.servicios.CreacionPizzaServicio;   // 👈 nuevo import
import com.example.PizzUMBurgUM.servicios.CarritoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CreacionPizzaServicio creacionPizzaServicio;

    @Autowired
    CreacionHamburguesaServicio creacionHamburguesaServicio;

    @Autowired
    private CreacionServicio creacionServicio;

    @Autowired
    private CarritoServicio carritoServicio;

    @Autowired
    private com.example.PizzUMBurgUM.servicios.PedidoServicio pedidoServicio;

    @Autowired
    private ProductoServicio productoServicio;

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
        } else {
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
            model.addAttribute("hamburguesasRecientes", clienteServicio.obtenerHamburguesasRecientes(cliente));
            model.addAttribute("pizzasRecientes", clienteServicio.obtenerPizzasRecientes(cliente));
            List<Favorito> favoritosReales = favoritoServicio.obtenerFavoritosPorCliente(cliente);
            model.addAttribute("favoritos", favoritosReales);

            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));

            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));
            model.addAttribute("totalFavoritos", favoritosReales.size());

            model.addAttribute("userEmail", cliente.getEmail());
            model.addAttribute("userName", cliente.getNombreUsuario());
        } else {
            model.addAttribute("hamburguesasRecientes", List.of());
            model.addAttribute("pizzasRecientes", List.of());
            model.addAttribute("favoritos", List.of());
            model.addAttribute("totalHamburguesas", 0);
            model.addAttribute("totalPizzas", 0);
            model.addAttribute("totalFavoritos", 0);
        }

        return "cliente/panel";
    }

    @GetMapping("/crear-hamburguesa")
    public String crearHamburguesa(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        model.addAttribute("carnes", creacionHamburguesaServicio.obtenerCarnes());
        model.addAttribute("panesHamburguesa", creacionHamburguesaServicio.obtenerPanesHamburguesa());
        model.addAttribute("ingredientesHamburguesa", creacionHamburguesaServicio.obtenerIngredientesHamburguesa());
        model.addAttribute("aderezosHamburguesa", creacionHamburguesaServicio.obtenerAderezosHamburguesa());

        return "cliente/crearHamburguesa";
    }

    @GetMapping("/crear-pizza")
    public String crearPizza(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        // Carga SOLO ingredientes activos
        model.addAttribute("masas", creacionPizzaServicio.obtenerMasas());
        model.addAttribute("salsas", creacionPizzaServicio.obtenerSalsas());
        model.addAttribute("quesosPizza", creacionPizzaServicio.obtenerQuesosPizza());
        model.addAttribute("toppingsGenerales", creacionPizzaServicio.obtenerToppingsGenerales());

        return "cliente/crearPizza"; // o "cliente/crear-pizza" según se llame tu html
    }

    @GetMapping("/productos/bebidas")
    @ResponseBody
    public ResponseEntity<?> obtenerBebidas() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "items", productoServicio.listarActivosPorTipo("B")
        ));
    }

    @GetMapping("/productos/acompanamientos")
    @ResponseBody
    public ResponseEntity<?> obtenerAcompanamientos() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "items", productoServicio.listarActivosPorTipo("AC")
        ));
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

        List<Favorito> pizzasFavoritas = favoritos.stream()
                .filter(f -> "PIZZA".equals(f.getTipo()))
                .collect(Collectors.toList());

        List<Favorito> hamburguesasFavoritas = favoritos.stream()
                .filter(f -> "HAMBURGUESA".equals(f.getTipo()))
                .collect(Collectors.toList());

        model.addAttribute("favoritos", favoritos);
        model.addAttribute("favoritosPizza", pizzasFavoritas);
        model.addAttribute("favoritosHamburguesa", hamburguesasFavoritas);
        model.addAttribute("totalFavoritos", favoritos.size());
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
    public String ordenes(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        var pedidoActivo = pedidoServicio.obtenerPedidoActivo(cliente);
        model.addAttribute("pedidoActivo", pedidoActivo);
        return "cliente/ordenes";
    }

    @GetMapping("/carrito")
    public String carrito(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/auth/login";
        }
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente != null) {
            List<CarritoItem> items = carritoServicio.obtenerCarritoDelCliente(cliente);
            model.addAttribute("carritoItems", items);
            model.addAttribute("total", carritoServicio.calcularTotalCarrito(cliente));
        }
        return "cliente/carrito";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/auth/login";
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        if (cliente != null) {
            model.addAttribute("cliente", cliente);

            List<Favorito> favoritos = favoritoServicio.obtenerFavoritosPorCliente(cliente);
            model.addAttribute("favoritos", favoritos);
            model.addAttribute("totalFavoritos", favoritos.size());

            model.addAttribute("historialPizzas", clienteServicio.obtenerPizzasRecientes(cliente));
            model.addAttribute("historialHamburguesas", clienteServicio.obtenerHamburguesasRecientes(cliente));

            model.addAttribute("totalPizzas", clienteServicio.contarTotalPizzas(cliente));
            model.addAttribute("totalHamburguesas", clienteServicio.contarTotalHamburguesas(cliente));
            
            // Agregar cantidad de items en el carrito
            int cantidadCarrito = carritoServicio.obtenerTotalItems(cliente);
            model.addAttribute("cantidadCarrito", cantidadCarrito);
        } else {
            model.addAttribute("favoritos", java.util.Collections.emptyList());
            model.addAttribute("totalFavoritos", 0);
            model.addAttribute("totalPizzas", 0);
            model.addAttribute("totalHamburguesas", 0);
            model.addAttribute("historialPizzas", java.util.Collections.emptyList());
            model.addAttribute("historialHamburguesas", java.util.Collections.emptyList());
        }

        return "cliente/perfil";
    }

    /**
     * Endpoint para guardar una creación (pizza o hamburguesa) cuando se agrega al carrito
     * Crea la creación y la agrega al carrito en BD
     */
    @PostMapping("/guardar-creacion")
    @ResponseBody
    public ResponseEntity<?> guardarCreacion(
            @RequestBody CreacionDto creacionDto,
            HttpSession session) {

        try {
            // Obtener cliente de la sesión
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            // Crear la creación (verifica duplicados internamente)
            Creacion creacion = creacionServicio.crearCreacionDesdeDto(creacionDto, cliente);

            // Agregar al carrito en BD
            carritoServicio.agregarAlCarrito(cliente, creacion, 1);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Creación agregada al carrito exitosamente",
                    "creacionId", creacion.getId_creacion(),
                    "nombre", creacion.getNombre()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "Error al guardar la creación: " + e.getMessage()));
        }
    }

    /**
     * Obtiene los items del carrito del cliente autenticado
     */
    @GetMapping("/carrito-items")
    @ResponseBody
    public ResponseEntity<?> obtenerCarrito(HttpSession session) {
        try {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            List<CarritoItem> items = carritoServicio.obtenerCarritoDelCliente(cliente);
            Double total = carritoServicio.calcularTotalCarrito(cliente);
            Integer cantidadItems = carritoServicio.obtenerTotalItems(cliente);
            var pedidoActivo = pedidoServicio.obtenerPedidoActivo(cliente);

            // Map a DTO simple para evitar problemas de serializaci��n/lazy
            List<CarritoItemDto> itemsDto = items.stream().map(item -> new CarritoItemDto(
                    item.getId(),
                    item.getCreacion() != null ? item.getCreacion().getId_creacion() : null,
                    item.getCreacion() != null ? item.getCreacion().getNombre() : "",
                    item.getCreacion() != null ? item.getCreacion().getDescripcion() : "",
                    item.getCreacion() != null ? item.getCreacion().getTipo() : ' ',
                    item.getPrecioUnitario(),
                    item.getCantidad()
            )).toList();

            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Carrito obtenido correctamente");
            resp.put("items", itemsDto);
            resp.put("total", total);
            resp.put("cantidadItems", cantidadItems);
            if (pedidoActivo != null) {
                resp.put("pedidoActivo", Map.of(
                        "id", pedidoActivo.getIdPedido(),
                        "estado", pedidoActivo.getEstado()
                ));
            }

            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "Error al obtener carrito: " + e.getMessage()));
        }
    }

    /**
     * Actualiza la cantidad de un item en el carrito
     */
    @PutMapping("/carrito-item/{id}/cantidad")
    @ResponseBody
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            HttpSession session) {
        try {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            carritoServicio.actualizarCantidad(id, cantidad);
            Double nuevoTotal = carritoServicio.calcularTotalCarrito(cliente);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cantidad actualizada",
                    "nuevoTotal", nuevoTotal
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Elimina un item del carrito
     */
    @DeleteMapping("/carrito-item/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarDelCarrito(
            @PathVariable Long id,
            HttpSession session) {
        try {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            carritoServicio.eliminarDelCarrito(id);
            Double nuevoTotal = carritoServicio.calcularTotalCarrito(cliente);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item eliminado del carrito",
                    "nuevoTotal", nuevoTotal
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Vacía el carrito del cliente
     */
    @PostMapping("/vaciar-carrito")
    @ResponseBody
    public ResponseEntity<?> vaciarCarrito(HttpSession session) {
        try {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            carritoServicio.vaciarCarrito(cliente);
            return ResponseEntity.ok(Map.of("success", true, "message", "Carrito vaciado"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/pedido-activo")
    @ResponseBody
    public ResponseEntity<?> obtenerPedidoActivo(HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Usuario no autenticado"));
        }
        var pedidoActivo = pedidoServicio.obtenerPedidoActivo(cliente);
        if (pedidoActivo == null) {
            return ResponseEntity.ok(Map.of("success", true, "activo", false));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("activo", true);
        data.put("pedido", Map.of(
                "id", pedidoActivo.getIdPedido(),
                "estado", pedidoActivo.getEstado(),
                "fecha", pedidoActivo.getFecha()
        ));
        data.put("puedeCancelar", "EN_COLA".equalsIgnoreCase(pedidoActivo.getEstado()));
        return ResponseEntity.ok(data);
    }

    @PostMapping("/pedido/{id}/cancelar")
    @ResponseBody
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Usuario no autenticado"));
        }
        try {
            var pedido = pedidoServicio.cancelarPedido(cliente, id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Pedido cancelado",
                    "pedidoId", pedido.getIdPedido()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Realiza un pedido a partir del carrito
     */
    @PostMapping("/realizar-pedido")
    @ResponseBody
    public ResponseEntity<?> realizarPedido(
            @RequestBody(required = false) com.example.PizzUMBurgUM.dto.PedidoRequestDto pedidoRequest,
            HttpSession session) {
        try {
            Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
            if (cliente == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("success", false, "message", "Usuario no autenticado"));
            }

            // Bloquea si ya tiene un pedido activo (no entregado ni cancelado)
            if (pedidoServicio.obtenerPedidoActivo(cliente) != null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Ya tienes un pedido activo"));
            }

            Double costoEnvio = (pedidoRequest != null && pedidoRequest.costoEnvio() != null)
                    ? pedidoRequest.costoEnvio()
                    : 0.0;

            // Crear pedido desde carrito
            com.example.PizzUMBurgUM.entidades.Pedido pedido = pedidoServicio.crearPedidoDesdeCarrito(
                    cliente, 
                    pedidoRequest != null ? pedidoRequest.acompanamientos() : null,
                    pedidoRequest != null ? pedidoRequest.bebidas() : null,
                    costoEnvio
            );

            // Vaciar carrito
            carritoServicio.vaciarCarrito(cliente);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Pedido realizado exitosamente",
                    "pedidoId", pedido.getIdPedido(),
                    "total", pedido.getPrecioTotal()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "Error al realizar pedido: " + e.getMessage()));
        }
    }

}
