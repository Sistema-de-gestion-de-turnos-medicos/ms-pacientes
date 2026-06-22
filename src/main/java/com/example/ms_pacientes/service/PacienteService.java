package com.example.ms_pacientes.service;

import com.example.ms_pacientes.Cliente.UsuarioCliente;

import com.example.ms_pacientes.dto.*;

import com.example.ms_pacientes.exception.ResourceNotFoundException;

import com.example.ms_pacientes.model.Paciente;

import com.example.ms_pacientes.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    PacienteService.class
            );

    private final PacienteRepository
            pacienteRepository;

    private final UsuarioCliente
            usuarioCliente;

    public List<PacienteResponsedto>
    listar() {

        logger.info(
                "Listando pacientes"
        );

        return pacienteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PacienteResponsedto guardar(
            PacienteRequestdto dto) {

        logger.info(
                "Guardando paciente"
        );

        usuarioCliente.buscarUsuario(
                dto.getUsuarioId()
        );

        Paciente paciente =
                Paciente.builder()

                        .nombres(dto.getNombres())

                        .apellidos(dto.getApellidos())

                        .dni(dto.getDni())

                        .telefono(dto.getTelefono())

                        .direccion(dto.getDireccion())

                        .usuarioId(
                                dto.getUsuarioId()
                        )

                        .build();

        return mapToResponse(
                pacienteRepository.save(
                        paciente
                )
        );
    }

    public PacienteResponsedto buscarPorId(
            Long id) {

        Paciente paciente =
                pacienteRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Paciente no encontrado"
                                )
                        );

        return mapToResponse(paciente);
    }

    public void eliminar(Long id) {

        Paciente paciente =
                pacienteRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Paciente no encontrado"
                                )
                        );

        pacienteRepository.delete(paciente);
    }

    private PacienteResponsedto
    mapToResponse(
            Paciente paciente) {

        return PacienteResponsedto
                .builder()

                .id(paciente.getId())

                .nombres(
                        paciente.getNombres()
                )

                .apellidos(
                        paciente.getApellidos()
                )

                .dni(
                        paciente.getDni()
                )

                .telefono(
                        paciente.getTelefono()
                )

                .direccion(
                        paciente.getDireccion()
                )

                .usuarioId(
                        paciente.getUsuarioId()
                )

                .build();
    }
}
