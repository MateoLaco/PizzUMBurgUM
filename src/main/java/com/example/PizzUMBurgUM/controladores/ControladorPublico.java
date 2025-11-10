package com.example.PizzUMBurgUM.controladores;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPublico {
    @GetMapping("/inicio")
    public String inicio(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // no crear si no existe
        if (session != null) {
            if (session.getAttribute("clienteLogueado") != null) {
                return "redirect:/cliente/panel";
            }
            if (session.getAttribute("funcionarioLogueado") != null) {
                return "redirect:/funcionario/panel";
            }
        }
        return "publico/inicio";
    }

}
