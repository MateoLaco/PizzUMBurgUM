package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.dto.PedidoExtraDto;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.entidades.PedidoExtraItem;
import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.CarritoItemRepositorio;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PedidoServicio {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private CarritoItemRepositorio carritoItemRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    /**
     * Crea un pedido a partir del carrito del cliente
     */
    public Pedido crearPedidoDesdeCarrito(Cliente cliente,
                                          List<PedidoExtraDto> acompanamientos,
                                          List<PedidoExtraDto> bebidas,
                                          Double costoEnvio) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        // Obtener todas las creaciones del carrito
        var carritoItems = carritoItemRepositorio.findByCliente(cliente);

        if (carritoItems.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        // Crear conjunto de creaciones
        Set<Creacion> creaciones = new HashSet<>();
        Double totalPrecio = 0.0;

        for (var item : carritoItems) {
            creaciones.add(item.getCreacion());
            totalPrecio += item.getPrecioUnitario() * item.getCantidad();
        }

        // Extras
        List<PedidoExtraItem> acompanamientosItems = mapearExtras(acompanamientos, "AC");
        List<PedidoExtraItem> bebidasItems = mapearExtras(bebidas, "B");

        totalPrecio += acompanamientosItems.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();
        totalPrecio += bebidasItems.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();

        // Agregar costo de envío
        if (costoEnvio != null) {
            totalPrecio += costoEnvio;
        }

        // Crear el pedido
        Pedido pedido = Pedido.builder()
                .estado("EN_COLA")
                .acompanamientos(acompanamientosItems)
                .bebidas(bebidasItems)
                .costoEnvio(costoEnvio)
                .precioTotal(totalPrecio)
                .creaciones(creaciones)
                .cliente(cliente)
                .build();

        return pedidoRepositorio.save(pedido);
    }

    public Pedido cancelarPedido(Cliente cliente, Long pedidoId) {
        Pedido pedido = obtenerPedido(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        if (pedido.getCliente() == null || !pedido.getCliente().getIdUsuario().equals(cliente.getIdUsuario())) {
            throw new IllegalArgumentException("No autorizado para cancelar este pedido");
        }
        if (!"EN_COLA".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException("El pedido solo puede cancelarse cuando está en cola");
        }
        pedido.setEstado("CANCELADO");
        return pedidoRepositorio.save(pedido);
    }

    public Pedido obtenerPedidoActivo(Cliente cliente) {
        List<String> enCurso = List.of("EN_COLA", "EN_PREPARACION", "EN_CAMINO");
        return pedidoRepositorio
                .findTopByClienteAndEstadoInOrderByFechaDesc(cliente, enCurso)
                .orElse(null);
    }

    private List<PedidoExtraItem> mapearExtras(List<PedidoExtraDto> extras, String tipoEsperado) {
        List<PedidoExtraItem> resultado = new ArrayList<>();
        if (extras == null) {
            return resultado;
        }

        for (PedidoExtraDto extra : extras) {
            if (extra == null || extra.productoId() == null) {
                continue;
            }

            Producto producto = productoRepositorio.findById(extra.productoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto extra no encontrado: " + extra.productoId()));

            if (!producto.getTipo().equalsIgnoreCase(tipoEsperado)) {
                throw new IllegalArgumentException("Tipo de producto inválido para extra: " + producto.getNombre());
            }

            int cantidad = (extra.cantidad() != null && extra.cantidad() > 0) ? extra.cantidad() : 1;

            resultado.add(PedidoExtraItem.builder()
                    .productoId(producto.getId_producto())
                    .nombre(producto.getNombre())
                    .tipo(producto.getTipo())
                    .cantidad(cantidad)
                    .precioUnitario((double) producto.getPrecio())
                    .build());
        }

        return resultado;
    }

    /**
     * Obtiene un pedido por ID
     */
    public Pedido obtenerPedido(Long id) {
        return pedidoRepositorio.findById(id).orElse(null);
    }

    /**
     * Actualiza el estado de un pedido
     */
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPedido(id);
        if (pedido != null) {
            pedido.setEstado(nuevoEstado);
            return pedidoRepositorio.save(pedido);
        }
        return null;
    }
}
