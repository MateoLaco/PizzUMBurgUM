package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.dto.DetalleDto;
import com.example.PizzUMBurgUM.dto.TicketDto;
import com.example.PizzUMBurgUM.entidades.*;
import com.example.PizzUMBurgUM.repositorios.ClienteCreacionRepositorio;
import com.example.PizzUMBurgUM.repositorios.ClienteRepositorio;
import com.example.PizzUMBurgUM.repositorios.CreacionRepositorio;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreacionServicio {

    @Autowired
    private CreacionRepositorio creacionRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private ClienteCreacionRepositorio clienteCreacionRepositorio;

    public Creacion agregarCreacion(
            Long id_cliente,
            char tipo_creacion,
            List<Long> ids_productos,
            boolean favorito
    ){
        Cliente cliente = clienteRepositorio.findById(id_cliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Set<Producto> productos = new HashSet<>(productoRepositorio.findAllById(ids_productos));

        Creacion creacion = null;

        if (tipo_creacion == 'P' || tipo_creacion == 'H') {

            creacion = Creacion.builder()
                    .tipoCreacion(tipo_creacion)
                    .creador(cliente)
                    .productos(productos)
                    .build();

            creacionRepositorio.save(creacion);

        }

        if (creacion != null) {
            if (favorito) {
                ClienteCreacionId ccid = new ClienteCreacionId(
                        cliente.getIdUsuario(),
                        creacion.getIdCreacion()
                );

                ClienteCreacion cc = ClienteCreacion.builder()
                        .id(ccid)
                        .cliente(cliente)
                        .creacion(creacion)
                        .favorito(true)
                        .build();

                clienteCreacionRepositorio.save(cc);

                creacion.getRelacionesConClientes().add(cc);

            }
        }

        return creacion;

    }

    public Creacion actualizarCreacion(Creacion unaCreacion){
        if (creacionRepositorio.existsById(unaCreacion.getIdCreacion())) {
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

    public TicketDto generarTicket(Long idCreacion) {

        Creacion creacion = creacionRepositorio.findById(idCreacion).orElseThrow();

        List<DetalleDto> detalle = creacion.getProductos().stream()
                .map(p -> new DetalleDto(p.getNombre(), (int) p.getPrecio()))
                .toList();

        int total = detalle.stream()
                .mapToInt(DetalleDto::costo)
                .sum();

        return new TicketDto(
                creacion.getIdCreacion(),
                creacion.getCreador().getEmail(),
                detalle,
                total
        );
    }

}