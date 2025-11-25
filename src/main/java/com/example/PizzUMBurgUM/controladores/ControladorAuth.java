package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.RegistroClienteDto;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@Controller
@RequestMapping("/auth")
public class ControladorAuth {

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private FuncionarioServicio funcionarioServicio;

    // MOSTRAR LOGIN
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "registro", required = false) String registro,
                               @RequestParam(value = "next", required = false) String nextFromQuery,
                               HttpSession session,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        if (registro != null) {
            model.addAttribute("mensaje", "¡Registro exitoso!");
        }

        // Prioridad: si viene ?next=... en la URL → usarlo
        if (nextFromQuery != null && !nextFromQuery.isBlank()) {
            model.addAttribute("next", nextFromQuery);
        } else {
            // Si no, mirar si el interceptor dejó algo en sesión
            String next = (String) session.getAttribute("NEXT_URL");
            if (next != null) {
                model.addAttribute("next", next);
            }
        }

        return "auth/login";
    }

    // PROCESAR LOGIN
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String contrasena,
                                @RequestParam(value = "next", required = false) String nextParam,
                                HttpSession session,
                                Model model) {

        Cliente cliente = clienteServicio.findByEmail(email);
        Funcionario funcionario = funcionarioServicio.findByEmail(email);

        boolean okCliente = cliente != null && cliente.getContrasena().equals(contrasena);
        boolean okFunc = funcionario != null && funcionario.getContrasena().equals(contrasena);

        if (!(okCliente || okFunc)) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "auth/login";
        }

        if (okCliente) session.setAttribute("clienteLogueado", cliente);
        if (okFunc)    session.setAttribute("funcionarioLogueado", funcionario);

        // 1) next del form
        String next = nextParam;
        // 2) o lo que guardó el interceptor en sesión
        if (next == null) next = (String) session.getAttribute("NEXT_URL");
        // Limpieza
        session.removeAttribute("NEXT_URL");

        // Seguridad básica → no permitir redirecciones externas
        if (next != null && next.startsWith("/")) {
            return "redirect:" + next;
        }

        // Fallback por tipo de usuario
        return okCliente ? "redirect:/cliente/panel" : "redirect:/admin/panel";
    }

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("form", new RegistroClienteDto());
        return "auth/register";
    }

    // PROCESAR REGISTRO (tu versión, ya sin conflicto)
    @PostMapping("/register")
    public String procesarRegistro(
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String confirmarContrasena,
            @RequestParam String metodoPago,
            @RequestParam String direccion,
            @RequestParam(name = "tel") String telefono,
            @RequestParam(name = "codigoPais") String codigoPais,
            @RequestParam(name = "diaNacimiento") Integer diaNacimiento,
            @RequestParam(name = "mesNacimiento") Integer mesNacimiento,
            @RequestParam(name = "anioNacimiento") Integer anioNacimiento,
            @RequestParam String numeroTarjeta,
            @RequestParam String nombreTarjeta,
            @RequestParam String vencimientoTarjeta,
            HttpSession session,
            Model model) {

        try {
            // 1) Validación de contraseñas
            if (!contrasena.equals(confirmarContrasena)) {
                model.addAttribute("error", "Las contraseñas no coinciden");
                return "auth/register";
            }

            // 2) Verificar email único
            if (clienteServicio.findByEmail(email) != null) {
                model.addAttribute("error", "El email ya está registrado");
                return "auth/register";
            }

            // 3) Validar fecha de nacimiento
            if (diaNacimiento == null || mesNacimiento == null || anioNacimiento == null) {
                model.addAttribute("error", "Fecha de nacimiento incompleta");
                return "auth/register";
            }

            // 4) Crear fecha de nacimiento
            LocalDate fechaNacimiento = LocalDate.of(anioNacimiento, mesNacimiento, diaNacimiento);

            // 5) Validar que la fecha no sea futura
            if (fechaNacimiento.isAfter(LocalDate.now())) {
                model.addAttribute("error", "La fecha de nacimiento no puede ser futura");
                return "auth/register";
            }

            // 6) Formatear teléfono completo
            String telefonoCompleto = codigoPais + " " + telefono;

            // 7) Limpiar y formatear número de tarjeta (quitar espacios)
            String numeroTarjetaLimpio = numeroTarjeta.replaceAll("\\s+", "");

            // 8) Crear y guardar cliente (fechaRegistro se setea en @PrePersist)
            Cliente clienteNuevo = Cliente.builder()
                    .nombreUsuario(nombreUsuario.trim())
                    .email(email.trim().toLowerCase())
                    .contrasena(contrasena)
                    .metodoPago(metodoPago)
                    .direccion(direccion.trim())
                    .telefono(telefonoCompleto)
                    .fechaNacimiento(fechaNacimiento)
                    .numeroTarjeta(numeroTarjetaLimpio)
                    .nombreTarjeta(nombreTarjeta.trim())
                    .vencimientoTarjeta(vencimientoTarjeta)
                    .build();

            Cliente clienteRegistrado = clienteServicio.guardarCliente(clienteNuevo);

            // 9) Establecer en sesión
            session.setAttribute("clienteLogueado", clienteRegistrado);

            // 10) Redirigir al panel
            return "redirect:/cliente/panel";

        } catch (DateTimeException e) {
            model.addAttribute("error", "Fecha de nacimiento inválida");
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", "Error en el registro: " + e.getMessage());
            return "auth/register";
        }
    }

    // CERRAR SESIÓN
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("clienteLogueado");
        session.removeAttribute("funcionarioLogueado");
        session.invalidate();
        return "redirect:/inicio";
    }
}
