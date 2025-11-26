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
    private FuncionarioRepositorio funcionarioRepositorio;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PizzUmBurgUmApplication.class, args);
        PizzUmBurgUmApplication app = ctx.getBean(PizzUmBurgUmApplication.class);
        app.runInCommandLine();
    }

    public void runInCommandLine() {
            Funcionario funcionario = Funcionario.builder()
                    .nombreUsuario("ADMIN")
                    .contrasena("pizzumburgum")
                    .email("admin@correo")
                    .rol("ADMIN")
                    .build();

            funcionarioRepositorio.save(funcionario);
    }

}
