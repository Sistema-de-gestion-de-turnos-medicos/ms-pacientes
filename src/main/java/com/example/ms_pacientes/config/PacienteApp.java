package com.example.ms_pacientes.config;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PacienteApp {

    public static void main(String[] args) {

        SpringApplication.run(
                PacienteApp.class,
                args
        );
    }
}