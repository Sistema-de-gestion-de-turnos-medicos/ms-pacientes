package com.example.ms_pacientes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteResponsedto {

    private Long id;

    private String nombres;

    private String apellidos;

    private String dni;

    private String telefono;

    private String direccion;

    private Long usuarioId;
}