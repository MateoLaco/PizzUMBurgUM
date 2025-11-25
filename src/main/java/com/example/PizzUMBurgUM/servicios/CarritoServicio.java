package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.CarritoItem;
import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Creacion;
import com.example.PizzUMBurgUM.repositorios.CarritoItemRepositorio;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarritoServicio {

    @Autowired
    private CarritoItemRepositorio carritoItemRepositorio;

    @Autowired
    private CreacionRepositorio creacionRepositorio;

    /**
     * Agrega una creación al carrito o incrementa la cantidad si ya existe
     */
    public CarritoItem agregarAlCarrito(Cliente cliente, Creacion creacion, Integer cantidad) {
        
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (creacion == null) {
            throw new IllegalArgumentException("La creación es obligatoria");
        }

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Verificar si ya existe este item en el carrito
        Optional<CarritoItem> existente = carritoItemRepositorio
                .findByClienteAndCreacion(cliente, creacion);

        if (existente.isPresent()) {
            // Si existe, incrementar la cantidad
            CarritoItem item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoItemRepositorio.save(item);
        } else {
            // Si no existe, crear nuevo item
            CarritoItem nuevoItem = CarritoItem.builder()
                    .cliente(cliente)
                    .creacion(creacion)
                    .cantidad(cantidad)
                    .precioUnitario(creacion.getPrecio())
                    .build();
            return carritoItemRepositorio.save(nuevoItem);
        }
    }

    /**
     * Obtiene todos los items del carrito de un cliente
     */
    public List<CarritoItem> obtenerCarritoDelCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        List<CarritoItem> items = carritoItemRepositorio.findByClienteOrderByFechaAgregadoDesc(cliente);
        
        // Inicializar los datos para evitar LazyInitializationException
        items.forEach(item -> {
            if (item.getCreacion() != null) {
                item.getCreacion().getId_creacion();
                item.getCreacion().getNombre();
                item.getCreacion().getDescripcion();
                item.getCreacion().getTipo();
                item.getCreacion().getPrecio();
            }
        });
        
        return items;
    }

    /**
     * Obtiene un item específico del carrito
     */
    public Optional<CarritoItem> obtenerItemDelCarrito(Long id) {
        return carritoItemRepositorio.findById(id);
    }

    /**
     * Actualiza la cantidad de un item en el carrito
     */
    public CarritoItem actualizarCantidad(Long itemId, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        CarritoItem item = carritoItemRepositorio.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item del carrito no encontrado"));

        item.setCantidad(nuevaCantidad);
        return carritoItemRepositorio.save(item);
    }

    /**
     * Elimina un item del carrito
     */
    public void eliminarDelCarrito(Long itemId) {
        carritoItemRepositorio.deleteById(itemId);
    }

    /**
     * Elimina todos los items del carrito de un cliente
     */
    public void vaciarCarrito(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        carritoItemRepositorio.deleteByCliente(cliente);
    }

    /**
     * Calcula el total del carrito
     */
    public Double calcularTotalCarrito(Cliente cliente) {
        List<CarritoItem> items = obtenerCarritoDelCliente(cliente);
        return items.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }

    /**
     * Obtiene la cantidad total de items en el carrito
     */
    public Integer obtenerTotalItems(Cliente cliente) {
        List<CarritoItem> items = obtenerCarritoDelCliente(cliente);
        return items.stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }
}
