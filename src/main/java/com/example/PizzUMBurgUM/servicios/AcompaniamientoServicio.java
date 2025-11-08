package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Acompaniamiento;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.entidades.PedidoAcompaniamiento;
import com.example.PizzUMBurgUM.entidades.PedidoAcompaniamientoId;
import com.example.PizzUMBurgUM.repositorios.AcompaniamientoRepositorio;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AcompaniamientoServicio {

    @Autowired
    private AcompaniamientoRepositorio acompaniamientoRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    /* ===================== CRUD ACOMPANIAMIENTO ===================== */

    public Acompaniamiento agregarAcompaniamiento(Acompaniamiento a) {
        validarAcompaniamiento(a);
        return acompaniamientoRepositorio.save(a);
    }

    public Acompaniamiento agregarAcompaniamiento(String nombre, BigDecimal precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser >= 0");
        }
        Acompaniamiento nuevo = Acompaniamiento.builder()
                .nombre_acompaniamiento(nombre)
                .precio_acompaniamiento(precio)
                .build();
        return acompaniamientoRepositorio.save(nuevo);
    }

    public Acompaniamiento actualizarAcompaniamiento(Acompaniamiento a) {
        if (a == null || a.getId_acompaniamiento() == null) {
            throw new RuntimeException("Acompaniamiento inválido");
        }
        if (!acompaniamientoRepositorio.existsById(a.getId_acompaniamiento())) {
            throw new RuntimeException("No existe el acompaniamiento con id=" + a.getId_acompaniamiento());
        }
        validarAcompaniamiento(a);
        return acompaniamientoRepositorio.save(a);
    }

    public boolean eliminarAcompaniamiento(Long idAcompaniamiento) {
        if (idAcompaniamiento == null) return false;
        acompaniamientoRepositorio.deleteById(idAcompaniamiento);
        return true;
    }

    public List<Acompaniamiento> obtenerAcompaniamientos() {
        return acompaniamientoRepositorio.findAll();
    }

    public Optional<Acompaniamiento> obtenerAcompaniamientoPorId(Long id) {
        return acompaniamientoRepositorio.findById(id);
    }

    /* ===================== RELACIÓN CON PEDIDO ===================== */

    public Pedido agregarAcompaniamientoAPedido(Long idPedido, Long idAcompaniamiento, int cantidad) {
        if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser > 0");

        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        Acompaniamiento a = acompaniamientoRepositorio.findById(idAcompaniamiento)
                .orElseThrow(() -> new RuntimeException("Acompaniamiento no encontrado"));

        PedidoAcompaniamientoId id = new PedidoAcompaniamientoId(pedido.getIdPedido(), a.getId_acompaniamiento());

        // Si la línea ya existe, acumulamos cantidad
        Optional<PedidoAcompaniamiento> existenteOpt = pedido.getAcompaniamientos().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst();

        if (existenteOpt.isPresent()) {
            PedidoAcompaniamiento existente = existenteOpt.get();
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            PedidoAcompaniamiento linea = PedidoAcompaniamiento.builder()
                    .id(id)
                    .pedido(pedido)
                    .acompaniamiento(a)
                    .cantidad(cantidad)
                    .precioUnitario(a.getPrecio_acompaniamiento()) // BigDecimal congelado al momento de agregar
                    .build();
            pedido.getAcompaniamientos().add(linea);
        }

        return pedidoRepositorio.save(pedido);
    }

    public Pedido actualizarCantidadAcompaniamiento(Long idPedido, Long idAcompaniamiento, int nuevaCantidad) {
        if (nuevaCantidad <= 0) throw new RuntimeException("La cantidad debe ser > 0");

        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoAcompaniamientoId id = new PedidoAcompaniamientoId(idPedido, idAcompaniamiento);
        PedidoAcompaniamiento linea = pedido.getAcompaniamientos().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El acompaniamiento no está en el pedido"));

        linea.setCantidad(nuevaCantidad);
        return pedidoRepositorio.save(pedido);
    }

    public Pedido quitarAcompaniamientoDePedido(Long idPedido, Long idAcompaniamiento) {
        Pedido pedido = pedidoRepositorio.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoAcompaniamientoId id = new PedidoAcompaniamientoId(idPedido, idAcompaniamiento);
        boolean removed = pedido.getAcompaniamientos().removeIf(l -> l.getId().equals(id));
        if (!removed) throw new RuntimeException("El acompaniamiento no estaba en el pedido");

        return pedidoRepositorio.save(pedido);
    }

    /* ===================== HELPERS ===================== */

    private void validarAcompaniamiento(Acompaniamiento a) {
        if (a == null) throw new RuntimeException("El acompaniamiento no puede ser null");
        if (a.getNombre_acompaniamiento() == null || a.getNombre_acompaniamiento().isBlank()) {
            throw new RuntimeException("El nombre no puede estar vacío");
        }
        if (a.getPrecio_acompaniamiento() == null || a.getPrecio_acompaniamiento().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El precio debe ser >= 0");
        }
    }
}
