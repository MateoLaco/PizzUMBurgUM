package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.MetodoPago;
import com.example.PizzUMBurgUM.repositorios.MetodoPagoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MetodoPagoServicio {

    @Autowired
    private MetodoPagoRepositorio metodoPagoRepositorio;

    public MetodoPago crearMetodoPago(Cliente cliente, String tipo, String numero, String nombreTitular, String vencimiento, String cvv, boolean principal) {
        MetodoPago mp = MetodoPago.builder()
                .cliente(cliente)
                .tipo(tipo)
                .numero(numero)
                .nombreTitular(nombreTitular)
                .vencimiento(vencimiento)
                .cvv(cvv)
                .principal(principal)
                .build();

        if (principal) {
            metodoPagoRepositorio.findByClienteAndPrincipalTrue(cliente)
                    .ifPresent(existing -> existing.setPrincipal(false));
        }

        return metodoPagoRepositorio.save(mp);
    }

    public List<MetodoPago> listarPorCliente(Cliente cliente) {
        return metodoPagoRepositorio.findByClienteOrderByPrincipalDescFechaAltaDesc(cliente);
    }

    public MetodoPago marcarComoPrincipal(Cliente cliente, Long metodoId) {
        MetodoPago mp = metodoPagoRepositorio.findById(metodoId)
                .filter(m -> m.getCliente().getIdUsuario().equals(cliente.getIdUsuario()))
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado"));

        metodoPagoRepositorio.findByClienteAndPrincipalTrue(cliente)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(mp.getId())) {
                        existing.setPrincipal(false);
                        metodoPagoRepositorio.save(existing);
                    }
                });

        mp.setPrincipal(true);
        return metodoPagoRepositorio.save(mp);
    }

    public boolean eliminar(Cliente cliente, Long metodoId) {
        return metodoPagoRepositorio.findById(metodoId)
                .filter(m -> m.getCliente().getIdUsuario().equals(cliente.getIdUsuario()))
                .map(mp -> {
                    boolean eraPrincipal = mp.isPrincipal();
                    metodoPagoRepositorio.delete(mp);
                    if (eraPrincipal) {
                        metodoPagoRepositorio.findByClienteOrderByPrincipalDescFechaAltaDesc(cliente)
                                .stream()
                                .findFirst()
                                .ifPresent(rest -> {
                                    rest.setPrincipal(true);
                                    metodoPagoRepositorio.save(rest);
                                });
                    }
                    return true;
                })
                .orElse(false);
    }
}
