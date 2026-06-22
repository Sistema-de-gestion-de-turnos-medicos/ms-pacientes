package com.example.ms_pacientes.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "pacientes")

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor

public class Paciente {

    @Id

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long id;

    @Column(nullable = false)

    private String nombres;

    @Column(nullable = false)

    private String apellidos;

    @Column(
            nullable = false,
            unique = true
    )

    private String dni;

    @Column(nullable = false)

    private String telefono;

    @Column(nullable = false)

    private String direccion;

    @Column(nullable = false)

    private Long usuarioId;
}


