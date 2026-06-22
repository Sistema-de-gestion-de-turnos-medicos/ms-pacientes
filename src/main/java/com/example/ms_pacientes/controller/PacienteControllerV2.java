package com.example.ms_pacientes.controller;

import com.example.ms_pacientes.assemblers.PacienteModelAssembler;
import com.example.ms_pacientes.dto.PacienteRequestdto;
import com.example.ms_pacientes.dto.PacienteResponsedto;
import com.example.ms_pacientes.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes V2", description = "Gestión de pacientes con HATEOAS")
public class PacienteControllerV2 {

    private final PacienteService pacienteService;
    private final PacienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los pacientes")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public CollectionModel<EntityModel<PacienteResponsedto>> listar() {
        List<EntityModel<PacienteResponsedto>> pacientes = pacienteService.listar()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pacientes,
                linkTo(methodOn(PacienteControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Buscar paciente por ID",
        description = "Retorna los datos de un paciente específico según su identificador"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Paciente encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Paciente encontrado",
                    value = "{ \"id\": 1, \"nombres\": \"María\", \"apellidos\": \"González\", \"dni\": \"12345678\", \"telefono\": \"987654321\", \"direccion\": \"Av. Siempre Viva 123\", \"usuarioId\": 5, \"_links\": { \"self\": { \"href\": \"/api/v2/pacientes/1\" }, \"pacientes\": { \"href\": \"/api/v2/pacientes\" } } }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Paciente no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"timestamp\": \"2026-06-21T10:15:30\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Paciente no encontrado\" }"
                )
            )
        )
    })
    public EntityModel<PacienteResponsedto> buscar(@PathVariable Long id) {
        return assembler.toModel(pacienteService.buscarPorId(id));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Crear un nuevo paciente",
        description = "Registra un nuevo paciente verificando previamente que el usuario asociado exista en ms-usuario"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Paciente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Paciente creado",
                    value = "{ \"id\": 1, \"nombres\": \"María\", \"apellidos\": \"González\", \"dni\": \"12345678\", \"telefono\": \"987654321\", \"direccion\": \"Av. Siempre Viva 123\", \"usuarioId\": 5, \"_links\": { \"self\": { \"href\": \"/api/v2/pacientes/1\" } } }"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(
            responseCode = "404",
            description = "El usuario asociado no existe en ms-usuario",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"timestamp\": \"2026-06-21T10:15:30\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuario no encontrado\" }"
                )
            )
        )
    })
    public ResponseEntity<EntityModel<PacienteResponsedto>> guardar(
            @Valid @RequestBody PacienteRequestdto dto) {
        PacienteResponsedto response = pacienteService.guardar(dto);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar paciente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
