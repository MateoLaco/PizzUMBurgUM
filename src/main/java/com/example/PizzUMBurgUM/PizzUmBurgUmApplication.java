package com.example.PizzUMBurgUM;

import com.example.PizzUMBurgUM.entidades.*;
import com.example.PizzUMBurgUM.repositorios.*;
import com.example.PizzUMBurgUM.servicios.CreacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Component
@EnableJpaRepositories(basePackages = "com.example.PizzUMBurgUM.repositorios")
@EntityScan(basePackages = "com.example.PizzUMBurgUM.entidades")
public class PizzUmBurgUmApplication {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private CreacionRepositorio creacionRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Autowired
    private CreacionServicio creacionServicio;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PizzUmBurgUmApplication.class, args);
        PizzUmBurgUmApplication app = ctx.getBean(PizzUmBurgUmApplication.class);
        app.runInCommandLine();
    }

    public void runInCommandLine() {
        try {
            Producto p1 = Producto.builder()
                    .nombre("Tomate")
                    .precio(100)
                    .tipo("P")
                    .build();

            Producto p2 = Producto.builder()
                    .nombre("Pepperoni")
                    .precio(200)
                    .tipo("P")
                    .build();

            productoRepositorio.save(p1);
            productoRepositorio.save(p2);

            Cliente cliente1 = Cliente.builder()
                    .email("f@a")
                    .telefono("99999999")
                    .contrasena("pepepepe")
                    .nombreUsuario("FrancoPapa")
                    .direccion("sdoadjas")
                    .fechaNacimiento(LocalDate.now())
                    .metodoPago("VISA")
                    .build();

            clienteRepositorio.save(cliente1);

            List<Long> prods = List.of(p1.getId_producto(), p2.getId_producto());

            Creacion c1 = creacionServicio.agregarCreacion(cliente1.getIdUsuario(),'P',prods);

            Funcionario funcionario = Funcionario.builder()
                    .nombreUsuario("Juan")
                    .contrasena("pepepepe")
                    .email("juean@mail")
                    .rol("ADMIN")
                    .build();

            Funcionario funcionario1 = Funcionario.builder()
                    .nombreUsuario("Mateo")
                    .contrasena("pepepepe")
                    .email("m@op")
                    .rol("OPERADOR")
                    .build();

            funcionarioRepositorio.save(funcionario);
            funcionarioRepositorio.save(funcionario1);

            if (c1 != null) {
                Pedido pedido = Pedido.builder()
                        .fecha(LocalDate.now())
                        .creaciones(Set.of(c1))
                        .costoEnvio(100.0)
                        .estado("EN_COLA")
                        .build();

                pedidoRepositorio.save(pedido);
            }
        } catch (Exception e) {
            System.err.println("Error inicializando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
