package com.example.ms_pacientes.dto;



import lombok.Data;

@Data
public class Usuariodto {

    private Long id;

    private String nombre;

    private String email;

    private String rol;
}