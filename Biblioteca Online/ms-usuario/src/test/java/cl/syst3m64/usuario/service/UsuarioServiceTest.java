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

import cl.syst3m64.usuario.dto.UsuarioRequestDTO;
import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.dto.EstadoDTO;
import cl.syst3m64.usuario.client.EstadoFeignClient;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.repository.UsuarioRepository;
import cl.syst3m64.usuario.service.impl.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de UsuarioService")
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Rol rolEjemplo;
    private Usuario usuarioEjemplo;
    private UsuarioRequestDTO requestEjemplo;

    @BeforeEach
    void setUp() {
        rolEjemplo = new Rol(1L, "ADMIN");
        usuarioEjemplo = new Usuario(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rolEjemplo, 1L);
        requestEjemplo = new UsuarioRequestDTO("12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", 1L, 1L);
    }

    @Test
    @DisplayName("obtenerTodosLosUsuarios() retorna la lista de todos los usuarios")
    void obtenerTodosLosUsuarios_debeRetornarLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombres());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodosLosUsuarios() debe retornar lista vacia cuando no hay usuarios")
    void obtenerTodosLosUsuarios_debeRetornarListaVacia_siNoHayUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerUsuarioPorId() retorna el usuario cuando existe")
    void obtenerUsuarioPorId_debeRetornarUsuario_cuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("12345678-9", resultado.get().getRut());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("crearUsuario() retorna el usuario guardado DTO")
    void crearUsuario_debeRetornarUsuarioGuardado() {
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoDTO());
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        
        Usuario toSave = new Usuario(null, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rolEjemplo, 1L);
        when(usuarioRepository.save(toSave)).thenReturn(usuarioEjemplo);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(requestEjemplo);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
        verify(usuarioRepository, times(1)).save(toSave);
    }
}

