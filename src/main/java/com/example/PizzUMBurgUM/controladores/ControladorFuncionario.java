package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/funcionario")
public class ControladorFuncionario {

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("funcionarioLogueado") != null;
    }

    @GetMapping("/panel")
    public String panelPrincipal(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/auth/login";
        }
        Funcionario funcionario = (Funcionario) session.getAttribute("funcionarioLogeado");
        return "admin/panel";
    }
}
