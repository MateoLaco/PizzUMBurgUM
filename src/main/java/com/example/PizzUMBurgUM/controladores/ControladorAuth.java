package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.RegistroClienteDto;
import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        if (registro != null) {
            model.addAttribute("mensaje","¡Registro exitoso!");
        }
        return "auth/login";
    }

    // PROCESAR LOGIN
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {

        Cliente cliente = clienteServicio.findByEmail(email);
        Funcionario funcionario = funcionarioServicio.findByEmail(email);

        if (cliente != null && cliente.getContrasena().equals(contrasena)) {
            // Login exitoso - guardar en sesión
            session.setAttribute("clienteLogueado", cliente);
            return "redirect:/cliente/panel";
        } else if (funcionario != null && funcionario.getContrasena().equals(contrasena)) {
            session.setAttribute("funcionarioLogueado", funcionario);
            return "redirect:/funcionario/panel";
        } else {
            // Login fallido
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "auth/login";
        }
    }

    // Muestra el formulario con un DTO vacío
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
        // 1) Cross-field: contraseñas
        if (form.getContrasena() != null && form.getConfirmarContrasena() != null
                && !form.getContrasena().equals(form.getConfirmarContrasena())) {
            binding.addError(new FieldError("form", "confirmarContrasena", "Las contraseñas no coinciden"));
        }

        // 2) Email único
        if (form.getEmail() != null && clienteServicio.findByEmail(form.getEmail()) != null) {
            binding.addError(new FieldError("form", "email", "El email ya está registrado"));
        }

        // 3) Normalizar teléfono y chequear E.164 (≤ 15 dígitos totales)
        String local = form.getTel() == null ? "" : form.getTel().replaceAll("\\s+", "");

        String codigo = form.getCodigoPais() == null ? "" : form.getCodigoPais().replaceAll("\\s+", "");

        String fullE164 = (codigo + local).replace("+", ""); // solo dígitos para contar

        if (!fullE164.matches("\\d+")) {
            binding.addError(new FieldError("form", "tel", "Teléfono inválido"));
        } else if (fullE164.length() > 15) {
            binding.addError(new FieldError("form", "tel", "Demasiados dígitos (máx. 15 con código país)"));
        }

        // 4) Normalizar tarjeta (quitar espacios) y validación adicional opcional (Luhn)
        String tarjeta = form.getNumeroTarjeta() == null ? "" : form.getNumeroTarjeta().replaceAll("\\s+", "");
        if (!tarjeta.matches("\\d{13,19}")) {
            binding.addError(new FieldError("form", "numeroTarjeta", "La tarjeta debe tener 13 a 19 dígitos"));
        }
        // (Opcional) si querés, implementamos Luhn más tarde y marcamos error si falla.

        if (binding.hasErrors()) {
            model.addAttribute("error", "Revisa los campos marcados en rojo.");
            return "auth/register";
        }

        // 5) Mapear DTO -> Entidad
        LocalDate fnac = LocalDate.of(form.getAnioNacimiento(), form.getMesNacimiento(), form.getDiaNacimiento());
        String telefonoE164 = codigo + local; // con el + (para almacenar visualmente)
        if (!telefonoE164.startsWith("+")) telefonoE164 = "+" + telefonoE164;

        Cliente clienteNuevo = Cliente.builder()
                .nombreUsuario(form.getNombreUsuario().trim())
                .email(form.getEmail().trim())
                .contrasena(form.getContrasena())      // en prod: encriptar
                .metodoPago(form.getMetodoPago())
                .direccion(form.getDireccion().trim())
                .fechaNacimiento(fnac)
                .telefono(telefonoE164)                // E.164
                .numeroTarjeta(tarjeta)                // sin espacios
                .build();

        // 6) Guardar (service NO debe tirar RuntimeException por reglas ya validadas aquí)
        Cliente clienteRegistrado = clienteServicio.guardarCliente(clienteNuevo);

        // 7) Loguear y redirigir
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
