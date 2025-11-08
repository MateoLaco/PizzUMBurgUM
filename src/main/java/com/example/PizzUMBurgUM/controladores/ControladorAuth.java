package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import org.springframework.ui.Model;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.servicios.ClienteServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    // MOSTRAR REGISTRO
    @GetMapping("/register")
    public String mostrarRegistro() {
        return "auth/register";
    }

    // PROCESAR REGISTRO (SIMPLIFICADO)
    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String nombreUsuario,
                                   @RequestParam String email,
                                   @RequestParam String contrasena,
                                   @RequestParam String metodoPago,
                                   @RequestParam String direccion,
                                   @RequestParam (name = "tel") String telefono,
                                   @RequestParam(name = "codigoPais") String codigoPais,
                                   @RequestParam(name = "diaNacimiento") Integer diaNacimiento,
                                   @RequestParam(name = "mesNacimiento") Integer mesNacimiento,
                                   @RequestParam(name = "anioNacimiento") Integer anioNacimiento,
                                   @RequestParam String numeroTarjeta,
                                   HttpSession session,
                                   Model model) {

        try {
            // 1. Primero verificar si el email ya existe
            if (clienteServicio.findByEmail(email) != null) {
                model.addAttribute("error", "El email ya está registrado");
                return "auth/register";
            }
            String telefonoCompleto = codigoPais + " " + telefono;
            LocalDate fechaNacimiento = LocalDate.of(anioNacimiento, mesNacimiento, diaNacimiento);


            Cliente clienteNuevo = Cliente.builder()
                    .nombreUsuario(nombreUsuario)
                    .email(email)
                    .contrasena(contrasena) // En producción, deberías encriptar esto
                    .metodoPago(metodoPago)
                    .direccion(direccion)
                    .telefono(telefono)
                    .fechaNacimiento(LocalDate.now()) // Fecha por defecto
                    .build();


            Cliente clienteRegistrado = clienteServicio.agregarCliente(clienteNuevo);

//
            session.setAttribute("clienteLogueado", clienteRegistrado);
            return "redirect:/cliente/panel";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "auth/register";
        }

    }

    // CERRAR SESIÓN
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("clienteLogueado");
        session.invalidate();
        return "redirect:/auth/login";
    }
}
