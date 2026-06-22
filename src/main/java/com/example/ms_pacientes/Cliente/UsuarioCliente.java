package com.example.ms_pacientes.Cliente;

import com.example.ms_pacientes.dto.Usuariodto;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuario")
public interface UsuarioCliente {

    @GetMapping("/api/usuarios/{id}")
    Usuariodto buscarUsuario(
            @PathVariable Long id
    );
}