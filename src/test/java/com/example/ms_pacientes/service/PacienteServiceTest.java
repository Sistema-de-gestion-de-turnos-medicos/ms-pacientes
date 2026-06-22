package com.example.ms_pacientes.service;

import com.example.ms_pacientes.Cliente.UsuarioCliente;
import com.example.ms_pacientes.dto.PacienteRequestdto;
import com.example.ms_pacientes.dto.PacienteResponsedto;
import com.example.ms_pacientes.dto.Usuariodto;
import com.example.ms_pacientes.exception.ResourceNotFoundException;
import com.example.ms_pacientes.model.Paciente;
import com.example.ms_pacientes.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UsuarioCliente usuarioCliente;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente pacienteEjemplo;
    private PacienteRequestdto requestEjemplo;
    private Usuariodto usuarioEjemplo;

    @BeforeEach
    void setUp() {
        pacienteEjemplo = Paciente.builder()
                .id(1L)
                .nombres("María")
                .apellidos("González")
                .dni("12345678")
                .telefono("987654321")
                .direccion("Av. Siempre Viva 123")
                .usuarioId(5L)
                .build();

        requestEjemplo = new PacienteRequestdto();
        requestEjemplo.setNombres("María");
        requestEjemplo.setApellidos("González");
        requestEjemplo.setDni("12345678");
        requestEjemplo.setTelefono("987654321");
        requestEjemplo.setDireccion("Av. Siempre Viva 123");
        requestEjemplo.setUsuarioId(5L);

        usuarioEjemplo = new Usuariodto();
        usuarioEjemplo.setId(5L);
        usuarioEjemplo.setNombre("María González");
        usuarioEjemplo.setEmail("maria@duoc.cl");
        usuarioEjemplo.setRol("ESTUDIANTE");
    }

    // ---------- listar() ----------

    @Test
    @DisplayName("Dado que existen pacientes, cuando se listan, entonces retorna la lista mapeada")
    void listar_conPacientesExistentes_retornaListaMapeada() {
        // Given
        when(pacienteRepository.findAll()).thenReturn(List.of(pacienteEjemplo));

        // When
        List<PacienteResponsedto> resultado = pacienteService.listar();

        // Then
        assertThat(resultado).hasSize(1);
        assertEquals("María", resultado.get(0).getNombres());
        assertEquals("12345678", resultado.get(0).getDni());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dado que no existen pacientes, cuando se listan, entonces retorna lista vacía")
    void listar_sinPacientes_retornaListaVacia() {
        // Given
        when(pacienteRepository.findAll()).thenReturn(List.of());

        // When
        List<PacienteResponsedto> resultado = pacienteService.listar();

        // Then
        assertThat(resultado).isEmpty();
    }

    // ---------- guardar() ----------

    @Test
    @DisplayName("Dado un paciente con usuario válido, cuando se guarda, entonces verifica el usuario y persiste")
    void guardar_conUsuarioValido_retornaPacienteGuardado() {
        // Given
        when(usuarioCliente.buscarUsuario(5L)).thenReturn(usuarioEjemplo);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteEjemplo);

        // When
        PacienteResponsedto resultado = pacienteService.guardar(requestEjemplo);

        // Then
        assertEquals("María", resultado.getNombres());
        assertEquals("12345678", resultado.getDni());
        assertEquals(5L, resultado.getUsuarioId());
        verify(usuarioCliente, times(1)).buscarUsuario(5L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Dado un usuario inexistente en ms-usuario, cuando se guarda, entonces propaga el error remoto y no persiste")
    void guardar_conUsuarioInexistente_propagaErrorYNoPersiste() {
        // Given
        when(usuarioCliente.buscarUsuario(5L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        // When & Then
        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pacienteService.guardar(requestEjemplo)
        );

        assertEquals("Usuario no encontrado", excepcion.getMessage());
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    // ---------- buscarPorId() ----------

    @Test
    @DisplayName("Dado un ID existente, cuando se busca, entonces retorna el paciente correspondiente")
    void buscarPorId_conIdExistente_retornaPaciente() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteEjemplo));

        // When
        PacienteResponsedto resultado = pacienteService.buscarPorId(1L);

        // Then
        assertEquals("María", resultado.getNombres());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Dado un ID inexistente, cuando se busca, entonces lanza ResourceNotFoundException")
    void buscarPorId_conIdInexistente_lanzaExcepcion() {
        // Given
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> pacienteService.buscarPorId(99L)
        );

        assertEquals("Paciente no encontrado", excepcion.getMessage());
    }

    // ---------- eliminar() ----------

    @Test
    @DisplayName("Dado un ID existente, cuando se elimina, entonces se borra del repositorio")
    void eliminar_conIdExistente_eliminaCorrectamente() {
        // Given
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteEjemplo));

        // When
        pacienteService.eliminar(1L);

        // Then
        verify(pacienteRepository, times(1)).delete(pacienteEjemplo);
    }

    @Test
    @DisplayName("Dado un ID inexistente, cuando se elimina, entonces lanza ResourceNotFoundException y no borra nada")
    void eliminar_conIdInexistente_lanzaExcepcionYNoElimina() {
        // Given
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> pacienteService.eliminar(99L)
        );

        verify(pacienteRepository, never()).delete(any(Paciente.class));
    }
}