package cl.syst3m64.estado.service;

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

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.EstadoRepository;
import cl.syst3m64.estado.repository.TipoEstadoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de GlobalService")
public class GlobalServiceTest {

    // crear mocks ficticios de los repositorios
    // debido a que no tenemos comunicación con MySQL a causa del Test
    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private TipoEstadoRepository tipoEstadoRepository;

    // crear una instancia REAL del GlobalService con los mocks inyectados
    @InjectMocks
    private GlobalService globalService;

    // Variables para datos de pruebas reutilizables entre TEST
    private TipoEstado tipoEstadoEjemplo;
    private Estado estadoEjemplo;

    // datos iniciales antes de que inicien los tests
    @BeforeEach
    void setUp() {
        tipoEstadoEjemplo = new TipoEstado(1L, "VENTA");
        estadoEjemplo = new Estado(1L, "ACTIVO", "Venta activa", tipoEstadoEjemplo);
    }

    // TEST UNIT - Estados

    @Test
    @DisplayName("obtenerTodosEstados() retorna la lista de todos los estados")
    void obtenerTodosEstados_debeRetornarListaDeEstados() {
        // el repositorio simulado retorna datos como si se estuviese conectando a MySQL
        when(estadoRepository.findAll()).thenReturn(List.of(estadoEjemplo));

        List<Estado> resultado = globalService.obtenerTodosEstados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());

        // verificar que el findAll() del repositorio fue llamado 1 vez
        verify(estadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodosEstados() debe retornar lista vacia cuando no hay estados en MySQL")
    void obtenerTodosEstados_debeRetornarListaVacia_siNoHayEstados() {
        when(estadoRepository.findAll()).thenReturn(List.of());

        List<Estado> resultado = globalService.obtenerTodosEstados();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerEstadoPorId() retorna el estado cuando existe")
    void obtenerEstadoPorId_debeRetornarEstado_cuandoExiste() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEjemplo));

        Optional<Estado> resultado = globalService.obtenerEstadoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());

        verify(estadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerEstadoPorId() retorna Optional vacio cuando el ID no existe")
    void obtenerEstadoPorId_debeRetornarVacio_cuandoNoExiste() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Estado> resultado = globalService.obtenerEstadoPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("guardarEstado() retorna el estado guardado con ID asignado")
    void guardarEstado_debeRetornarEstadoGuardado() {
        Estado sinId = new Estado(null, "CANCELADA", "Venta cancelada", tipoEstadoEjemplo);
        Estado conId = new Estado(2L, "CANCELADA", "Venta cancelada", tipoEstadoEjemplo);

        when(estadoRepository.save(sinId)).thenReturn(conId);

        Estado resultado = globalService.guardarEstado(sinId);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("CANCELADA", resultado.getNombre());

        verify(estadoRepository, times(1)).save(sinId);
    }

    // TEST UNIT - TipoEstados

    @Test
    @DisplayName("obtenerTipoEstados() retorna la lista de todos los tipos de estado")
    void obtenerTipoEstados_debeRetornarListaDeTipos() {
        when(tipoEstadoRepository.findAll()).thenReturn(List.of(tipoEstadoEjemplo));

        List<TipoEstado> resultado = globalService.obtenerTipoEstados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("VENTA", resultado.get(0).getNombre());

        verify(tipoEstadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTipoEstadosPorId() retorna el tipo de estado cuando existe")
    void obtenerTipoEstadosPorId_debeRetornarTipo_cuandoExiste() {
        when(tipoEstadoRepository.findById(1L)).thenReturn(Optional.of(tipoEstadoEjemplo));

        Optional<TipoEstado> resultado = globalService.obtenerTipoEstadosPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("VENTA", resultado.get().getNombre());

        verify(tipoEstadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("guardarTipoEstado() retorna el tipo de estado guardado con ID asignado")
    void guardarTipoEstado_debeRetornarTipoGuardado() {
        TipoEstado sinId = new TipoEstado(null, "ARRIENDO");
        TipoEstado conId = new TipoEstado(2L, "ARRIENDO");

        when(tipoEstadoRepository.save(sinId)).thenReturn(conId);

        TipoEstado resultado = globalService.guardarTipoEstado(sinId);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("ARRIENDO", resultado.getNombre());

        verify(tipoEstadoRepository, times(1)).save(sinId);
    }
}
