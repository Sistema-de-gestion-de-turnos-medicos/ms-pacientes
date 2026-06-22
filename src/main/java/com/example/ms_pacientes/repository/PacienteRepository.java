package com.example.ms_pacientes.repository;

import com.example.ms_pacientes.model.Paciente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository
        extends JpaRepository<Paciente, Long> {
}