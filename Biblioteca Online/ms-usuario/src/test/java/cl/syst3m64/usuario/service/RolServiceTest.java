package cl.syst3m64.usuario.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.usuario.dto.RolRequestDTO;
import cl.syst3m64.usuario.dto.RolResponseDTO;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.service.impl.RolServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de RolService")
public class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolEjemplo;

    @BeforeEach
    void setUp() {
        rolEjemplo = new Rol(1L, "ADMIN");
    }

    @Test
    @DisplayName("obtenerTodosLosRoles() retorna la lista de todos los roles DTO")
    void obtenerTodosLosRoles_debeRetornarLista() {
        when(rolRepository.findAll()).thenReturn(List.of(rolEjemplo));

        List<RolResponseDTO> resultado = rolService.obtenerTodosLosRoles();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getNombre());

        verify(rolRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerRolPorId() retorna el rol DTO cuando existe")
    void obtenerRolPorId_debeRetornarRol_cuandoExiste() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));

        Optional<RolResponseDTO> resultado = rolService.obtenerRolPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());

        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("crearRol() retorna el rol DTO guardado")
    void crearRol_debeRetornarRolGuardado() {
        RolRequestDTO request = new RolRequestDTO("CLIENTE");
        Rol sinId = new Rol(null, "CLIENTE");
        Rol conId = new Rol(2L, "CLIENTE");

        when(rolRepository.save(sinId)).thenReturn(conId);

        RolResponseDTO resultado = rolService.crearRol(request);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(rolRepository, times(1)).save(sinId);
    }
}

