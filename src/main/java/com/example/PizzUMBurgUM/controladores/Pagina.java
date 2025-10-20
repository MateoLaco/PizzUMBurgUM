package com.example.PizzUMBurgUM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Pagina {
    @GetMapping("/inicio")
    public String inicio(){
        return "index";
    }
}
