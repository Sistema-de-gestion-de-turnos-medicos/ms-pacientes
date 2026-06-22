package com.example.ms_pacientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<Map<String, Object>>
    manejarNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> error =
                new HashMap<>();

        error.put(
                "mensaje",
                ex.getMessage()
        );

        error.put(
                "status",
                404
        );

        error.put(
                "fecha",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>>
    manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    errores.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );
                });

        return new ResponseEntity<>(
                errores,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<Map<String, Object>>
    manejarRuntime(
            RuntimeException ex) {

        Map<String, Object> error =
                new HashMap<>();

        error.put(
                "mensaje",
                ex.getMessage()
        );

        error.put(
                "status",
                400
        );

        error.put(
                "fecha",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<Map<String, Object>>
    manejarGeneral(
            Exception ex) {

        Map<String, Object> error =
                new HashMap<>();

        error.put(
                "mensaje",
                "Error interno del servidor"
        );

        error.put(
                "detalle",
                ex.getMessage()
        );

        error.put(
                "status",
                500
        );

        error.put(
                "fecha",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
