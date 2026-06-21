package cl.syst3m64.resena.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.resena.dto.LibroDTO;
import cl.syst3m64.resena.dto.ResenaRequestDTO;
import cl.syst3m64.resena.dto.ResenaResponseDTO;
import cl.syst3m64.resena.dto.UsuarioDTO;
import cl.syst3m64.resena.exception.ResenaNotFoundException;
import cl.syst3m64.resena.client.LibroFeignClient;
import cl.syst3m64.resena.client.UsuarioFeignClient;
import cl.syst3m64.resena.model.Resena;
import cl.syst3m64.resena.repository.ResenaRepository;
import cl.syst3m64.resena.service.impl.ResenaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de ResenaService")
public class ResenaServiceTest {

    // crear mocks de los repositorios y feign clients
    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private LibroFeignClient libroFeignClient;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    // crear una instancia REAL del ResenaService con los mocks inyectados
    @InjectMocks
    private ResenaServiceImpl resenaService;


    // Variables para datos de pruebas reutilizables entre TEST
    private Resena resenaEjemplo;
    private ResenaRequestDTO dtoRequest;
    private LibroDTO libroDTO;
    private UsuarioDTO usuarioDTO;

    // datos iniciales antes de cada test
    @BeforeEach
    void setUp() {
        resenaEjemplo = new Resena(1L, 1L, 2L, "Gran libro",
                "Un clasico imprescindible", 5, LocalDate.of(2024, 1, 15), "ACTIVO");

        dtoRequest = new ResenaRequestDTO();
        dtoRequest.setIdLibro(1L);
        dtoRequest.setIdUsuario(2L);
        dtoRequest.setTitulo("Gran libro");
        dtoRequest.setComentario("Un clasico imprescindible");
        dtoRequest.setCalificacion(5);

        libroDTO = new LibroDTO();
        libroDTO.setIdLibro(1L);
        libroDTO.setTitulo("El Quijote");
        libroDTO.setAutor("Cervantes");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(2L);
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Perez");
    }

    // TEST UNIT

    @Test
    @DisplayName("listarTodas() retorna la lista de todas las resenas")
    void listarTodas_debeRetornarListaDeResenas() {
        // el repositorio simulado retorna datos como si se conectara a MySQL
        when(resenaRepository.findAll()).thenReturn(List.of(resenaEjemplo));
        when(libroFeignClient.obtenerLibroPorId(1L)).thenReturn(libroDTO);
        when(usuarioFeignClient.obtenerUsuarioPorId(2L)).thenReturn(usuarioDTO);

        // llamar a la funcion del servicio que quiero testear
        List<ResenaResponseDTO> resultado = resenaService.listarTodas();

        // criterios de aceptacion
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gran libro", resultado.get(0).getTitulo());
        assertEquals("El Quijote", resultado.get(0).getLibro().getTitulo());

        // verificar que findAll() del repositorio fue llamado 1 vez
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodas() debe retornar lista vacia cuando no hay resenas")
    void listarTodas_debeRetornarListaVacia_SiNoHayResenas() {
        // GIVEN
        when(resenaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<ResenaResponseDTO> resultado = resenaService.listarTodas();

        // criterios de aceptacion
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorId() debe retornar ResenaResponseDTO cuando la resena existe")
    void buscarPorId_debeRetornarResena_cuandoExiste() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaEjemplo));
        when(libroFeignClient.obtenerLibroPorId(1L)).thenReturn(libroDTO);
        when(usuarioFeignClient.obtenerUsuarioPorId(2L)).thenReturn(usuarioDTO);

        ResenaResponseDTO resultado = resenaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdResena());
        assertEquals("Gran libro", resultado.getTitulo());
        assertEquals(5, resultado.getCalificacion());

        verify(resenaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId() debe lanzar ResenaNotFoundException cuando no existe")
    void buscarPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResenaNotFoundException.class, () -> resenaService.buscarPorId(99L));

        verify(resenaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("crear() debe guardar y retornar ResenaResponseDTO con datos validos")
    void crear_debeRetornarResenaGuardada() {
        when(libroFeignClient.obtenerLibroPorId(1L)).thenReturn(libroDTO);
        when(usuarioFeignClient.obtenerUsuarioPorId(2L)).thenReturn(usuarioDTO);
        when(resenaRepository.existsByIdLibroAndIdUsuario(1L, 2L)).thenReturn(false);
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaEjemplo);

        ResenaResponseDTO resultado = resenaService.crear(dtoRequest);

        assertNotNull(resultado);
        assertEquals("Gran libro", resultado.getTitulo());
        assertEquals(5, resultado.getCalificacion());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    @DisplayName("crear() debe lanzar excepcion cuando el usuario ya reseno el libro")
    void crear_debeLanzarExcepcion_cuandoYaExisteResena() {
        when(libroFeignClient.obtenerLibroPorId(1L)).thenReturn(libroDTO);
        when(usuarioFeignClient.obtenerUsuarioPorId(2L)).thenReturn(usuarioDTO);
        when(resenaRepository.existsByIdLibroAndIdUsuario(1L, 2L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> resenaService.crear(dtoRequest));
    }

    @Test
    @DisplayName("eliminar() debe hacer soft delete cambiando el estado a ELIMINADO")
    void eliminar_debeCambiarEstadoAEliminado() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaEjemplo));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaEjemplo);

        resenaService.eliminar(1L);

        // verificar que el estado fue cambiado a ELIMINADO
        assertEquals("ELIMINADO", resenaEjemplo.getEstado());
        verify(resenaRepository, times(1)).save(resenaEjemplo);
    }
}

