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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // Procesa el registro manteniendo valores + errores de validación
    @PostMapping("/register")
    public String procesarRegistro(
            @Valid @ModelAttribute("form") RegistroClienteDto form,
            BindingResult binding,
            HttpSession session,
            Model model
    ) {
        // 1) Validación cruzada de contraseñas
        if (form.getContrasena() != null && form.getConfirmarContrasena() != null &&
                !form.getContrasena().equals(form.getConfirmarContrasena())) {
            binding.rejectValue("confirmarContrasena", "contrasena.mismatch", "Las contraseñas no coinciden");
        }

        // 2) Email único
        if (form.getEmail() != null && clienteServicio.findByEmail(form.getEmail()) != null) {
            binding.rejectValue("email", "email.duplicado", "El email ya está registrado");
        }

        if (binding.hasErrors()) {
            model.addAttribute("error", "Revisa los campos marcados.");
            return "auth/register";
        }

        // 3) Armar fecha de nacimiento
        LocalDate fechaNacimiento = LocalDate.of(
                form.getAnioNacimiento(),
                form.getMesNacimiento(),
                form.getDiaNacimiento()
        );

        // 4) Armar teléfono completo en formato internacional (ej: +59891234567)
        String telefonoCompleto = form.getCodigoPais() + form.getTel(); // tel ya viene sin espacios

        // 5) (Opcional) parsear vencimientoTarjeta si lo quisieras como YearMonth o LocalDate
        // String[] partes = form.getVencimientoTarjeta().split("/");
        // int mes = Integer.parseInt(partes[0]);
        // int anio = Integer.parseInt(partes[1]);
        // YearMonth vencimiento = YearMonth.of(anio, mes);

        // 6) Mapear a entidad Cliente (sin guardar CVV)
        Cliente clienteNuevo = Cliente.builder()
                .nombreUsuario(form.getNombreUsuario().trim())
                .email(form.getEmail().trim())
                .contrasena(form.getContrasena())          // en prod: encriptar
                .metodoPago(form.getMetodoPago())
                .direccion(form.getDireccion().trim())
                .fechaNacimiento(fechaNacimiento)
                .telefono(telefonoCompleto)
                .numeroTarjeta(form.getNumeroTarjeta())    // sin espacios
                // .nombreTarjeta(...) solo si tu entidad lo tiene
                // .vencimientoTarjeta(vencimiento) si tu entidad tiene ese campo
                .build();

        Cliente clienteRegistrado = clienteServicio.guardarCliente(clienteNuevo);
        session.setAttribute("clienteLogueado", clienteRegistrado);
        return "redirect:/cliente/panel";
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
