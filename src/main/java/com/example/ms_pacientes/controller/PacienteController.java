package com.example.ms_pacientes.controller;

import com.example.ms_pacientes.dto.*;

import com.example.ms_pacientes.service.PacienteService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService
            pacienteService;

    @GetMapping
    public ResponseEntity<
            List<PacienteResponsedto>>
    listar() {

        return ResponseEntity.ok(
                pacienteService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<
            PacienteResponsedto>
    buscar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pacienteService.buscarPorId(
                        id
                )
        );
    }

    @PostMapping
    public ResponseEntity<
            PacienteResponsedto>
    guardar(

            @Valid

            @RequestBody
            PacienteRequestdto dto) {

        return new ResponseEntity<>(

                pacienteService.guardar(
                        dto
                ),

                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    eliminar(
            @PathVariable Long id) {

        pacienteService.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}