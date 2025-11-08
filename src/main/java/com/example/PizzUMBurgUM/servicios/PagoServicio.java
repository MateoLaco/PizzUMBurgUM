package com.example.PizzUMBurgUM.servicios;

import com.example.PizzUMBurgUM.repositorios.PagoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoServicio {

    @Autowired
    private PagoRepositorio pagoRepositorio;


}
