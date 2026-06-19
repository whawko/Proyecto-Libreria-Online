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

import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.feign.EstadoFeignClient;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de GlobalService")
public class GlobalServiceTest {

    // crear mocks ficticios de los repositorios y del cliente feign
    // debido a que no tenemos comunicacion con MySQL ni con ms_estado a causa del Test
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    // crear una instancia REAL del GlobalService con los mocks inyectados
    @InjectMocks
    private GlobalService usuarioService;

    // Variables para datos de pruebas reutilizables entre TEST
    private Rol rolEjemplo;
    private Usuario usuarioEjemplo;

    // datos iniciales antes de que inicien los tests
    @BeforeEach
    void setUp() {
        rolEjemplo = new Rol(1L, "ADMIN");
        usuarioEjemplo = new Usuario(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rolEjemplo, 1L);
    }

    // TEST UNIT - Usuarios

    @Test
    @DisplayName("obtenerTodosLosUsuarios() retorna la lista de todos los usuarios")
    void obtenerTodosLosUsuarios_debeRetornarLista() {
        // el repositorio simulado retorna datos como si se estuviese conectando a MySQL
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombres());

        // verificar que el findAll() del repositorio fue llamado 1 vez
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodosLosUsuarios() debe retornar lista vacia cuando no hay usuarios en MySQL")
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
    @DisplayName("obtenerUsuarioPorId() retorna Optional vacio cuando el ID no existe")
    void obtenerUsuarioPorId_debeRetornarVacio_cuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerUsuarioPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crearUsuario() retorna el usuario guardado con ID asignado")
    void crearUsuario_debeRetornarUsuarioGuardado() {
        // simular que el feign retorna un objeto valido (estado existe en ms_estado)
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new Object());
        when(usuarioRepository.save(usuarioEjemplo)).thenReturn(usuarioEjemplo);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(usuarioEjemplo);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
        assertEquals("12345678-9", resultado.getRut());

        verify(usuarioRepository, times(1)).save(usuarioEjemplo);
    }

    // TEST UNIT - Roles

    @Test
    @DisplayName("obtenerTodosLosRoles() retorna la lista de todos los roles")
    void obtenerTodosLosRoles_debeRetornarLista() {
        when(rolRepository.findAll()).thenReturn(List.of(rolEjemplo));

        List<Rol> resultado = usuarioService.obtenerTodosLosRoles();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getNombre());

        verify(rolRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerRolPorId() retorna el rol cuando existe")
    void obtenerRolPorId_debeRetornarRol_cuandoExiste() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));

        Optional<Rol> resultado = usuarioService.obtenerRolPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());

        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("crearRol() retorna el rol guardado con ID asignado")
    void crearRol_debeRetornarRolGuardado() {
        Rol sinId = new Rol(null, "CLIENTE");
        Rol conId = new Rol(2L, "CLIENTE");

        when(rolRepository.save(sinId)).thenReturn(conId);

        Rol resultado = usuarioService.crearRol(sinId);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("CLIENTE", resultado.getNombre());

        verify(rolRepository, times(1)).save(sinId);
    }
}
