package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Funcionario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPublico {
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        // Si hay un cliente logueado, lo llevo a su panel
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente != null) {
            return "/cliente/panel";
        }

        // Si hay un funcionario logueado, lo llevo a su panel
        Funcionario funcionario = (Funcionario) session.getAttribute("funcionarioLogueado");
        if (funcionario != null) {
            return "/admin/panel";
        }

        // Si no hay nadie logueado, muestro el inicio normal
        return "publico/inicio"; // nombre de tu plantilla Thymeleaf de inicio
    }

}
