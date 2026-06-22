package com.example.ms_pacientes.exception;

public class ResourceNotFoundException
        extends RuntimeException {

    public ResourceNotFoundException(
            String mensaje) {

        super(mensaje);
    }
}