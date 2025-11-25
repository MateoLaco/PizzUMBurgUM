package com.example.PizzUMBurgUM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorCreacion {
    @GetMapping("/cliente/crearHamburguesa")
    public String crearHamburguesa() {
        return "cliente/crearHamburguesa";
    }

    @GetMapping("/cliente/crearPizza")
    public String crearPizza() {
        return "cliente/crearPizza";
    }
}

