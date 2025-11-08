package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoServicio {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    public Pedido agregarPedido(Pedido pedido) {
        if (pedido == null){
            throw new NullPointerException("Pedido no puede ser nulo.");
        }
        if (pedidoRepositorio.existsById(pedido.getIdPedido())){
            throw new RuntimeException("El pedido ya existe");
        }
        pedidoRepositorio.save(pedido);
        return pedido;
    }
}
