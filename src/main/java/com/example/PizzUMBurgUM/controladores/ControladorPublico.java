package com.example.PizzUMBurgUM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPublico {
    @GetMapping("/inicio")
    public String inicio() {
        return "publico/inicio";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "auth/register";
    }
}
