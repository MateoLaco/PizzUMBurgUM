package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.entidades.Pedido;
import com.example.PizzUMBurgUM.repositorios.FuncionarioRepositorio;
import com.example.PizzUMBurgUM.repositorios.PedidoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FuncionarioServicio {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    // ----------- LISTAR PEDIDOS CON FILTROS -----------

    public List<Pedido> listarPedidosFiltrados(String estado, LocalDate fecha) {

        boolean tieneEstado = estado != null && !estado.isBlank();
        boolean tieneFecha  = fecha != null;

        if (tieneEstado && tieneFecha) {
            return pedidoRepositorio.findByEstadoAndFecha(estado, fecha);
        }
        if (tieneEstado) {
            return pedidoRepositorio.findByEstado(estado);
        }
        if (tieneFecha) {
            return pedidoRepositorio.findByFecha(fecha);
        }

        return pedidoRepositorio.findAll();
    }

    // ----------- CAMBIAR ESTADO DEL PEDIDO -----------

    public void actualizarEstadoPedido(Long id, String nuevoEstado) {

        Pedido pedido = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pedido con id " + id));

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El nuevo estado no puede ser vacío");
        }

        nuevoEstado = nuevoEstado.toUpperCase();

        if (!nuevoEstado.equals("EN_COLA") &&
                !nuevoEstado.equals("EN_PREPARACION") &&
                !nuevoEstado.equals("EN_CAMINO") &&
                !nuevoEstado.equals("ENTREGADO")) {

            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        pedido.setEstado(nuevoEstado);
        pedidoRepositorio.save(pedido);
    }

    // ----------- GESTIÓN DE FUNCIONARIOS -----------

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepositorio.findAll();
    }

    public Funcionario crearFuncionario(Funcionario funcionario) {

        if (funcionario.getEmail() == null || funcionario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (funcionarioRepositorio.existsByEmail(funcionario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un funcionario con ese email");
        }

        return funcionarioRepositorio.save(funcionario);
    }

    public Funcionario actualizarFuncionario(Long id, Funcionario nuevosDatos) {

        Funcionario existente = funcionarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el funcionario con id " + id));

        // ⬅ ACÁ EL CAMBIO QUE PEDISTE
        existente.setNombreUsuario(nuevosDatos.getNombreUsuario());
        existente.setEmail(nuevosDatos.getEmail());
        // otros campos según tu modelo...

        return funcionarioRepositorio.save(existente);
    }

    public void eliminarFuncionario(Long id) {
        if (!funcionarioRepositorio.existsById(id)) {
            throw new RuntimeException("No existe el funcionario a eliminar");
        }
        funcionarioRepositorio.deleteById(id);
    }

    // ----------- ✔ AGREGADO AL FINAL: findByEmail -----------

    public Funcionario findByEmail(String email) {
        return funcionarioRepositorio.findByEmail(email);
    }
}
