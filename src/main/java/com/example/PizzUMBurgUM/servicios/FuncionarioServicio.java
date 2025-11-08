package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.entidades.Cliente;
import com.example.PizzUMBurgUM.entidades.Funcionario;
import com.example.PizzUMBurgUM.repositorios.FuncionarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioServicio {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    public Funcionario findByEmail(String email) {
        return funcionarioRepositorio.findByEmail(email);
    }
}
