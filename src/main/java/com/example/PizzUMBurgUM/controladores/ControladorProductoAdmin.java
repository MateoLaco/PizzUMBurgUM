package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.servicios.ProductoServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ControladorProductoAdmin {

    @Autowired
    private ProductoServicio productoServicio;

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("funcionarioLogueado") != null;
    }

    @GetMapping
    public String listarProductos(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        model.addAttribute("productos", productoServicio.listarTodos());
        model.addAttribute("productoNuevo", new Producto());
        return "admin/productos";
    }

    @PostMapping
    public String crearProducto(@ModelAttribute("productoNuevo") Producto producto,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (!verificarSesion(session)) return "redirect:/auth/login";

        try {
            productoServicio.crearProducto(producto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto creado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/admin/productos";
    }

    // 🔥 Nuevo: actualizar todos los "activo" con un solo botón
    @PostMapping("/actualizar-activos")
    public String actualizarActivos(@RequestParam(value = "activos", required = false) List<Long> idsActivos,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

        if (!verificarSesion(session)) return "redirect:/auth/login";

        try {
            productoServicio.actualizarActivosEnLote(idsActivos);
            redirectAttributes.addFlashAttribute("mensajeExito", "Estados de productos actualizados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/admin/productos";
    }
}