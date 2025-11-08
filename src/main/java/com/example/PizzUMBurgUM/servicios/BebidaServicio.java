package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Bebida;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.entidades.PedidoBebida;
import com.example.PizzUMBurgUM.entidades.PedidoBebidaId;
import com.example.PizzUMBurgUM.repositorios.BebidaRepositorio;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BebidaServicio {

    @Autowired
    private BebidaRepositorio bebidaRepositorio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    /* ===================== BEBIDA ===================== */

    // Función para registrar una nueva bebida
    public Bebida agregarBebida(String nombre, BigDecimal precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser >= 0");
        }
        Bebida nueva = Bebida.builder()
                .nombre_bebida(nombre)
                .precio_bebida(precio)
                .build();
        return bebidaRepositorio.save(nueva);
    }

    public Bebida actualizarBebida(Bebida b) {
        if (b == null || b.getId_bebida() == null) {
            throw new RuntimeException("Bebida inválida");
        }
        if (!bebidaRepositorio.existsById(b.getId_bebida())) {
            throw new RuntimeException("No existe la bebida con id=" + b.getId_bebida());
        }
        validarBebida(b);
        return bebidaRepositorio.save(b);
    }

    public boolean eliminarBebida(Long idBebida) {
        if (idBebida == null) return false;
        bebidaRepositorio.deleteById(idBebida);
        return true;
    }

    public List<Bebida> obtenerBebidas() {
        return bebidaRepositorio.findAll();
    }

    public Optional<Bebida> obtenerBebidaPorId(Long id) {
        return bebidaRepositorio.findById(id);
    }

    /* ===================== RELACIÓN CON PEDIDO ===================== */

    public Pedido agregarBebidaAPedido(Long idPedido, Long idBebida, int cantidad) {
        if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser > 0");

        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        Bebida b = bebidaRepositorio.findById(idBebida)
                .orElseThrow(() -> new RuntimeException("Bebida no encontrada"));

        PedidoBebidaId id = new PedidoBebidaId(pedido.getIdPedido(), b.getId_bebida());
        // Si ya existe, acumulamos cantidad
        Optional<PedidoBebida> existenteOpt = pedido.getBebidas().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();

        if (existenteOpt.isPresent()) {
            PedidoBebida existente = existenteOpt.get();
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            PedidoBebida linea = PedidoBebida.builder()
                    .id(id)
                    .pedido(pedido)
                    .bebida(b)
                    .cantidad(cantidad)
                    .precioUnitario(b.getPrecio_bebida()) // BigDecimal congelado
                    .build();
            pedido.getBebidas().add(linea);
        }

        return pedidoRepositorio.save(pedido);
    }

    public Pedido actualizarCantidadBebida(Long idPedido, Long idBebida, int nuevaCantidad) {
        if (nuevaCantidad <= 0) throw new RuntimeException("La cantidad debe ser > 0");

        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoBebidaId id = new PedidoBebidaId(idPedido, idBebida);
        PedidoBebida linea = pedido.getBebidas().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("La bebida no está en el pedido"));

        linea.setCantidad(nuevaCantidad);
        return pedidoRepositorio.save(pedido);
    }

    public Pedido quitarBebidaDePedido(Long idPedido, Long idBebida) {
        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoBebidaId id = new PedidoBebidaId(idPedido, idBebida);
        boolean removed = pedido.getBebidas().removeIf(l -> l.getId().equals(id));
        if (!removed) throw new RuntimeException("La bebida no estaba en el pedido");

        return pedidoRepositorio.save(pedido);
    }

    /* ===================== HELPERS ===================== */

    private void validarBebida(Bebida b) {
        if (b == null) throw new RuntimeException("La bebida no puede ser null");
        if (b.getNombre_bebida() == null || b.getNombre_bebida().isBlank()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        if (b.getPrecio_bebida() == null || b.getPrecio_bebida().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser >= 0");
        }
    }
}
