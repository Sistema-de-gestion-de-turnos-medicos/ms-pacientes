package com.example.ms_pacientes.assemblers;

import com.example.ms_pacientes.controller.PacienteControllerV2;
import com.example.ms_pacientes.dto.PacienteResponsedto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PacienteModelAssembler
        implements RepresentationModelAssembler<PacienteResponsedto, EntityModel<PacienteResponsedto>> {

    @Override
    public EntityModel<PacienteResponsedto> toModel(PacienteResponsedto paciente) {
        return EntityModel.of(paciente,
                linkTo(methodOn(PacienteControllerV2.class)
                        .buscar(paciente.getId())).withSelfRel(),
                linkTo(methodOn(PacienteControllerV2.class)
                        .listar()).withRel("pacientes"));
    }
}