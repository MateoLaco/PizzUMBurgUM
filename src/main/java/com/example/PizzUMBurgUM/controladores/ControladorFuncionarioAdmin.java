package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class ControladorFuncionarioAdmin {

    @Autowired
    private FuncionarioServicio funcionarioServicio;

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("funcionarioLogueado") != null;
    }

    @GetMapping
    public String listado(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");

        model.addAttribute("funcionarios", funcionarioServicio.listarFuncionarios());
        model.addAttribute("funcionarioActual", funcionarioActual);

        return "admin/usuarios";
    }

    @PostMapping("/crear")
    public String crear(
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String rol,
            Model model,
            HttpSession session
    ) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");

        try {
            Funcionario f = Funcionario.builder()
                    .nombreUsuario(nombreUsuario)
                    .email(email)
                    .contrasena(contrasena)
                    .rol(rol)
                    .build();

            funcionarioServicio.crearFuncionario(f, funcionarioActual);
            model.addAttribute("mensajeExito", "Funcionario creado correctamente");

        } catch (Exception e) {
            model.addAttribute("mensajeError", e.getMessage());
        }

        model.addAttribute("funcionarios", funcionarioServicio.listarFuncionarios());
        model.addAttribute("funcionarioActual", funcionarioActual);

        return "admin/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        Funcionario funcionarioActual = (Funcionario) session.getAttribute("funcionarioLogueado");

        try {
            funcionarioServicio.eliminarFuncionario(id, funcionarioActual);
            model.addAttribute("mensajeExito", "Funcionario eliminado");

        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }

        model.addAttribute("funcionarios", funcionarioServicio.listarFuncionarios());
        model.addAttribute("funcionarioActual", funcionarioActual);

        return "admin/usuarios";
    }
}
