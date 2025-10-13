package com.example.PizzUMBurgUM;

import com.example.PizzUMBurgUM.entidades.Producto;
import com.example.PizzUMBurgUM.repositorios.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
@EnableJpaRepositories(basePackages = "com.example.PizzUMBurgUM.repositorios")
@EntityScan(basePackages = "com.example.PizzUMBurgUM.entidades")
public class PizzUmBurgUmApplication {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PizzUmBurgUmApplication.class, args);
        PizzUmBurgUmApplication app = ctx.getBean(PizzUmBurgUmApplication.class);
        app.runInCommandLine();
    }

    public void runInCommandLine() {
        Producto p1 = Producto.builder()
                .idProducto(1)
                .nombre("Tomate")
                .precio(100)
                .tipo("Pizza")
                .build();

        productoRepositorio.save(p1);
    }

}
