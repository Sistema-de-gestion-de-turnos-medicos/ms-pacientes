package com.example.ms_pacientes.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PacienteRequestdto {

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String dni;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;

    @NotNull
    private Long usuarioId;
}
