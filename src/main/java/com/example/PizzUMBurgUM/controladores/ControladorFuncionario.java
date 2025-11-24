package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.servicios.FuncionarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ControladorFuncionario {

    @Autowired
    private FuncionarioServicio funcionarioServicio;

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("funcionarioLogueado") != null;
    }

    // --------- PANEL PRINCIPAL ---------

    @GetMapping("/panel")
    public String panel(Model model, HttpSession session) {
        if (!verificarSesion(session)) return "redirect:/auth/login";

        return "admin/panel";
    }

    // --------- LISTA DE PEDIDOS ---------

    @GetMapping("/pedidos")
    public String listarPedidos(@RequestParam(required = false) String estado,
                                @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                Model model,
                                HttpSession session) {

        if (!verificarSesion(session)) return "redirect:/auth/login";

        List<Pedido> pedidos = funcionarioServicio.listarPedidosFiltrados(estado, fecha);

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaSeleccionada", fecha);

        return "admin/pedidos"; // si usas vista separada; si no: "funcionario/panel"
    }

    // --------- CAMBIAR ESTADO DE UN PEDIDO ---------

    @PostMapping("/pedidos/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam String estado,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (!verificarSesion(session)) return "redirect:/auth/login";

        try {
            funcionarioServicio.actualizarEstadoPedido(id, estado);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Estado del pedido actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al actualizar estado: " + e.getMessage());
        }

        return "redirect:/admin/pedidos";
    }
}
