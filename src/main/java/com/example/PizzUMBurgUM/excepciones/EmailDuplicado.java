package com.example.PizzUMBurgUM.excepciones;

public class EmailDuplicado extends RuntimeException {
    public EmailDuplicado(String message) {
        super(message);
    }
}
