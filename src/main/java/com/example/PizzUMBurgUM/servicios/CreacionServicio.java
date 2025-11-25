package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.dto.CreacionDto;
import com.example.PizzUMBurgUM.dto.DetalleDto;
import com.example.PizzUMBurgUM.dto.TicketDto;
import com.example.PizzUMBurgUM.entidades.*;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CreacionServicio {

    @Autowired
    private CreacionRepositorio creacionRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;

    public Creacion agregarCreacion(
            Long id_cliente,
            char tipo_creacion,
            List<Long> ids_productos
    ){
        Cliente cliente = clienteRepositorio.findById(id_cliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Set<Producto> productos = new HashSet<>(productoRepositorio.findAllById(ids_productos));

        Creacion creacion = null;

        if (tipo_creacion == 'P' || tipo_creacion == 'H') {

            creacion = Creacion.builder()
                    .tipo(tipo_creacion)
                    .creador(cliente)
                    .productos(productos)
                    .fechaCreacion(LocalDate.now())
                    .build();

            creacionRepositorio.save(creacion);

        }

        return creacion;

    }

    /**
     * Crea una creación desde un DTO (usado cuando se agrega al carrito desde el frontend)
     * Si ya existe una creación del mismo cliente con la misma descripción, retorna la existente
     */
    public Creacion crearCreacionDesdeDto(CreacionDto creacionDto, Cliente cliente) {
        
        if (creacionDto == null || cliente == null) {
            throw new IllegalArgumentException("Los datos de la creación y el cliente son obligatorios");
        }

        if (creacionDto.nombre() == null || creacionDto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la creación es obligatorio");
        }

        if (creacionDto.tipo() != 'P' && creacionDto.tipo() != 'H') {
            throw new IllegalArgumentException("El tipo debe ser 'P' (Pizza) o 'H' (Hamburguesa)");
        }

        if (creacionDto.precio() == null || creacionDto.precio() < 0) {
            throw new IllegalArgumentException("El precio debe ser válido y mayor o igual a cero");
        }

        // VERIFICAR DUPLICADOS: Buscar creación existente con mismo cliente y descripción
        List<Creacion> creacionesExistentes = creacionRepositorio.findByCreador(cliente);
        for (Creacion existente : creacionesExistentes) {
            if (existente.getDescripcion() != null && 
                existente.getDescripcion().equals(creacionDto.descripcion()) &&
                existente.getTipo() == creacionDto.tipo()) {
                // Retornar la creación existente para evitar duplicados
                return existente;
            }
        }

        // Obtener los productos
        Set<Producto> productos = new HashSet<>();
        if (creacionDto.productosIds() != null && !creacionDto.productosIds().isEmpty()) {
            productos = new HashSet<>(productoRepositorio.findAllById(creacionDto.productosIds()));
        }

        // Crear la creación solo si no existe duplicado
        Creacion creacion = Creacion.builder()
                .nombre(creacionDto.nombre())
                .descripcion(creacionDto.descripcion())
                .precio(creacionDto.precio())
                .tipo(creacionDto.tipo())
                .productos(productos)
                .creador(cliente)
                .favorito(creacionDto.favorito())
                .fechaCreacion(LocalDate.now())
                .build();

        Creacion guardada = creacionRepositorio.save(creacion);
        // Inicializar los datos para evitar LazyInitializationException
        if (guardada.getProductos() != null) {
            guardada.getProductos().size();
        }
        return guardada;
    }

    public Creacion actualizarCreacion(Creacion unaCreacion){
        if (creacionRepositorio.existsById(unaCreacion.getId_creacion())) {
            return creacionRepositorio.save(unaCreacion);
        }
        return null;
    }

    public boolean eliminarCreacion(Long idCreacion){
        if (idCreacion != null) {
            creacionRepositorio.deleteById(idCreacion);
            return true;
        }
        return false;
    }

    public List<Creacion> obtenerCreaciones(){return creacionRepositorio.findAll();}

    public List<Creacion> obtenerCreacionesPorCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        return creacionRepositorio.findByCreador(cliente);
    }

    public TicketDto generarTicket(Long idCreacion) {

        Creacion creacion = creacionRepositorio.findById(idCreacion).orElseThrow();

        List<DetalleDto> detalle = creacion.getProductos().stream()
                .map(p -> new DetalleDto(p.getNombre(), (int) p.getPrecio()))
                .toList();

        int total = detalle.stream()
                .mapToInt(DetalleDto::costo)
                .sum();

        return new TicketDto(
                creacion.getId_creacion(),
                creacion.getCreador().getEmail(),
                detalle,
                total
        );
    }

}