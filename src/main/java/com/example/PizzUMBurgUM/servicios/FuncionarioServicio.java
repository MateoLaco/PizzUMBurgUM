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

    // ------------------ PEDIDOS ------------------

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

    public void actualizarEstadoPedido(Long id, String nuevoEstado) {

        Pedido pedido = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro el pedido con id " + id));

        if (pedido.getEstado() != null &&
                (pedido.getEstado().equalsIgnoreCase("ENTREGADO") ||
                 pedido.getEstado().equalsIgnoreCase("CANCELADO"))) {
            throw new IllegalArgumentException("No se puede modificar un pedido entregado o cancelado");
        }

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El nuevo estado no puede ser vacio");
        }

        nuevoEstado = nuevoEstado.toUpperCase();

        if (!nuevoEstado.equals("EN_COLA") &&
                !nuevoEstado.equals("EN_PREPARACION") &&
                !nuevoEstado.equals("EN_CAMINO") &&
                !nuevoEstado.equals("ENTREGADO")) {
            throw new IllegalArgumentException("Estado invalido: " + nuevoEstado);
        }

        pedido.setEstado(nuevoEstado);
        pedidoRepositorio.save(pedido);
    }    // ------------------ FUNCIONARIOS (ABM) ------------------

    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepositorio.findAll();
    }

    public Funcionario crearFuncionario(Funcionario funcionario, Funcionario funcionarioActual) {

        if (funcionarioActual == null) {
            throw new IllegalStateException("No hay funcionario autenticado para crear usuarios.");
        }

        if (funcionario.getNombreUsuario() == null || funcionario.getNombreUsuario().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (funcionario.getEmail() == null || funcionario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (funcionarioRepositorio.existsByEmail(funcionario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un funcionario con ese email");
        }

        if (funcionario.getContrasena() == null || funcionario.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseÃ±a es obligatoria");
        }

        if (funcionario.getRol() == null || funcionario.getRol().isBlank()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        // OPERADOR no puede crear ADMIN
        if (funcionarioActual.getRol() != null
                && funcionarioActual.getRol().equalsIgnoreCase("OPERADOR")
                && funcionario.getRol().equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("No tienes permisos para crear administradores.");
        }

        return funcionarioRepositorio.save(funcionario);
    }

    public Funcionario actualizarFuncionario(Long id, Funcionario nuevosDatos, Funcionario funcionarioActual) {

        if (funcionarioActual == null) {
            throw new IllegalStateException("No hay funcionario autenticado para actualizar usuarios.");
        }

        Funcionario existente = funcionarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el funcionario con id " + id));

        // Si quien edita es OPERADOR:
        if (funcionarioActual.getRol() != null &&
                funcionarioActual.getRol().equalsIgnoreCase("OPERADOR")) {

            // No puede editar admins
            if (existente.getRol() != null && !existente.getRol().equalsIgnoreCase("OPERADOR")) {
                throw new IllegalArgumentException("No tienes permisos para modificar administradores.");
            }

            // No puede cambiar el rol a ADMIN
            if (nuevosDatos.getRol() != null && !nuevosDatos.getRol().equalsIgnoreCase("OPERADOR")) {
                throw new IllegalArgumentException("No puedes asignar rol ADMIN.");
            }
        }

        if (nuevosDatos.getNombreUsuario() == null || nuevosDatos.getNombreUsuario().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (nuevosDatos.getEmail() == null || nuevosDatos.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (!existente.getEmail().equals(nuevosDatos.getEmail())
                && funcionarioRepositorio.existsByEmail(nuevosDatos.getEmail())) {
            throw new IllegalArgumentException("Ya existe un funcionario con ese email");
        }
        if (nuevosDatos.getContrasena() == null || nuevosDatos.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contraseÃ±a es obligatoria");
        }
        if (nuevosDatos.getRol() == null || nuevosDatos.getRol().isBlank()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        existente.setNombreUsuario(nuevosDatos.getNombreUsuario());
        existente.setEmail(nuevosDatos.getEmail());
        existente.setContrasena(nuevosDatos.getContrasena());
        existente.setRol(nuevosDatos.getRol());

        return funcionarioRepositorio.save(existente);
    }

    public void eliminarFuncionario(Long id, Funcionario funcionarioActual) {

        if (funcionarioActual == null) {
            throw new IllegalStateException("No hay funcionario autenticado para eliminar usuarios.");
        }

        Funcionario objetivo = funcionarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el funcionario a eliminar"));

        if (funcionarioActual.getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("No puedes eliminar tu propio usuario.");
        }

        if (funcionarioActual.getRol() != null
                && funcionarioActual.getRol().equalsIgnoreCase("OPERADOR")
                && objetivo.getRol() != null
                && !objetivo.getRol().equalsIgnoreCase("OPERADOR")) {
            throw new IllegalArgumentException("No tienes permisos para eliminar administradores.");
        }

        funcionarioRepositorio.delete(objetivo);
    }

    public Funcionario findByEmail(String email) {
        return funcionarioRepositorio.findByEmail(email);
    }

    // ------------------ PERFIL DEL FUNCIONARIO ------------------

    /** Actualiza solo el nombre de usuario del funcionario logueado */
    public Funcionario actualizarNombrePerfil(Funcionario funcionarioActual, String nuevoNombre) {
        if (funcionarioActual == null) {
            throw new IllegalStateException("No hay funcionario autenticado.");
        }
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacÃ­o.");
        }

        Funcionario existente = funcionarioRepositorio.findById(funcionarioActual.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No se encontrÃ³ el funcionario."));

        existente.setNombreUsuario(nuevoNombre.trim());

        return funcionarioRepositorio.save(existente);
    }

    /**
     * Cambia la contraseÃ±a del funcionario logueado verificando la contraseÃ±a actual.
     */
    public Funcionario cambiarContrasenaPerfil(Funcionario funcionarioActual,
                                               String contrasenaActual,
                                               String nuevaContrasena) {
        if (funcionarioActual == null) {
            throw new IllegalStateException("No hay funcionario autenticado.");
        }
        if (contrasenaActual == null || contrasenaActual.isBlank()) {
            throw new IllegalArgumentException("Debes ingresar la contraseÃ±a actual.");
        }
        if (nuevaContrasena == null || nuevaContrasena.isBlank()) {
            throw new IllegalArgumentException("La nueva contraseÃ±a no puede estar vacÃ­a.");
        }
        if (nuevaContrasena.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseÃ±a debe tener al menos 8 caracteres.");
        }

        Funcionario existente = funcionarioRepositorio.findById(funcionarioActual.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No se encontrÃ³ el funcionario."));

        if (!existente.getContrasena().equals(contrasenaActual)) {
            throw new IllegalArgumentException("La contraseÃ±a actual no es correcta.");
        }

        existente.setContrasena(nuevaContrasena);

        return funcionarioRepositorio.save(existente);
    }
}


