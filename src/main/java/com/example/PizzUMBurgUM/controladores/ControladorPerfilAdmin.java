package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/perfil")
public class ControladorPerfilAdmin {

    @Autowired
    private FuncionarioServicio funcionarioServicio;

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("funcionarioLogueado") != null;
    }

    @GetMapping
    public String verPerfil(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");
        model.addAttribute("funcionarioActual", funcionarioActual);

        return "admin/perfil";
    }

    @PostMapping("/actualizar-datos")
    public String actualizarDatos(
            @RequestParam String nombreUsuario,
            Model model,
            HttpSession session
    ) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");

        try {
            Funcionario actualizado = funcionarioServicio.actualizarNombrePerfil(funcionarioActual, nombreUsuario);
            session.setAttribute("funcionarioLogueado", actualizado);

            model.addAttribute("funcionarioActual", actualizado);
            model.addAttribute("mensajeExito", "Nombre de usuario actualizado correctamente.");
        } catch (Exception e) {
            model.addAttribute("funcionarioActual", funcionarioActual);
            model.addAttribute("mensajeError", e.getMessage());
        }

        return "admin/perfil";
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(
            @RequestParam("contrasenaActual") String contrasenaActual,
            @RequestParam("nuevaContrasena") String nuevaContrasena,
            @RequestParam("confirmarContrasena") String confirmarContrasena,
            Model model,
            HttpSession session
    ) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");

        try {
            if (nuevaContrasena == null || confirmarContrasena == null ||
                    !nuevaContrasena.equals(confirmarContrasena)) {
                throw new IllegalArgumentException("Las contraseñas nuevas no coinciden.");
            }

            Funcionario actualizado = funcionarioServicio.cambiarContrasenaPerfil(
                    funcionarioActual,
                    contrasenaActual,
                    nuevaContrasena
            );

            session.setAttribute("funcionarioLogueado", actualizado);

            model.addAttribute("funcionarioActual", actualizado);
            model.addAttribute("mensajeExito", "Contraseña actualizada correctamente.");
        } catch (Exception e) {
            model.addAttribute("funcionarioActual", funcionarioActual);
            model.addAttribute("mensajeError", e.getMessage());
        }

        return "admin/perfil";
    }
}

