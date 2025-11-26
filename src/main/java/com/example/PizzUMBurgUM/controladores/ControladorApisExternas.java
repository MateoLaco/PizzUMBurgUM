package com.example.PizzUMBurgUM.controladores;

import com.example.PizzUMBurgUM.dto.TicketVentaDto;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositorios.FuncionarioRepositorio;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/externo")
public class ControladorApisExternas {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @GetMapping("/dgi/tickets")
    public ResponseEntity<?> ticketsPorFecha(@RequestParam String fecha) {
        LocalDate dia;
        try {
            dia = LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Fecha invalida, formato esperado: yyyy-MM-dd");
        }

        List<Pedido> pedidos = pedidoRepositorio.findByFecha(dia);
        List<TicketVentaDto> respuesta = pedidos.stream()
                .map(this::mapearPedido)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/bps/usuarios")
    public ResponseEntity<?> conteoUsuarios() {
        long clientes = clienteRepositorio.count();
        long funcionarios = funcionarioRepositorio.count();

        return ResponseEntity.ok(
                java.util.Map.of(
                        "clientes", clientes,
                        "funcionarios", funcionarios,
                        "total", clientes + funcionarios
                )
        );
    }

    private TicketVentaDto mapearPedido(Pedido p) {
        List<String> bebidas = p.getBebidas() == null ? List.of() :
                p.getBebidas().stream()
                        .map(b -> (b.getNombre() != null ? b.getNombre() : "") + " x" + b.getCantidad())
                        .collect(Collectors.toList());

        List<String> acomp = p.getAcompanamientos() == null ? List.of() :
                p.getAcompanamientos().stream()
                        .map(a -> (a.getNombre() != null ? a.getNombre() : "") + " x" + a.getCantidad())
                        .collect(Collectors.toList());

        return new TicketVentaDto(
                p.getIdPedido(),
                p.getFecha(),
                p.getEstado(),
                p.getCliente() != null ? p.getCliente().getEmail() : null,
                p.getPrecioTotal(),
                p.getCostoEnvio(),
                p.getCreaciones() != null ? p.getCreaciones().size() : 0,
                bebidas,
                acomp
        );
    }
}
