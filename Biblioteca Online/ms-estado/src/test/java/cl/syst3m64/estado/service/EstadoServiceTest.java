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
import cl.syst3m64.estado.dto.EstadoRequestDTO;
import cl.syst3m64.estado.dto.EstadoResponseDTO;
import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.EstadoRepository;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import cl.syst3m64.estado.service.impl.EstadoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de EstadoService")
public class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private TipoEstadoRepository tipoEstadoRepository;

    @InjectMocks
    private EstadoServiceImpl estadoService;

    private TipoEstado tipoEstadoEjemplo;
    private Estado estadoEjemplo;

    @BeforeEach
    void setUp() {
        tipoEstadoEjemplo = new TipoEstado(1L, "VENTA");
        estadoEjemplo = new Estado(1L, "ACTIVO", "Venta activa", tipoEstadoEjemplo);
    }

    @Test
    @DisplayName("obtenerTodosEstados() retorna la lista de todos los estados en DTOs")
    void obtenerTodosEstados_debeRetornarListaDeEstados() {
        when(estadoRepository.findAll()).thenReturn(List.of(estadoEjemplo));

        List<EstadoResponseDTO> resultado = estadoService.obtenerTodosEstados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());
        assertEquals("VENTA", resultado.get(0).getTipoEstado().getNombre());

        verify(estadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerEstadoPorId() retorna el DTO del estado cuando existe")
    void obtenerEstadoPorId_debeRetornarEstado_cuandoExiste() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEjemplo));

        Optional<EstadoResponseDTO> resultado = estadoService.obtenerEstadoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());

        verify(estadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("guardarEstado() retorna el DTO del estado guardado")
    void guardarEstado_debeRetornarEstadoGuardado() {
        EstadoRequestDTO request = new EstadoRequestDTO("CANCELADA", "Venta cancelada", 1L);
        Estado conId = new Estado(2L, "CANCELADA", "Venta cancelada", tipoEstadoEjemplo);

        when(tipoEstadoRepository.findById(1L)).thenReturn(Optional.of(tipoEstadoEjemplo));
        // Mock save using Matchers or exact instance. We need to match whatever gets saved.
        // To be safe we mock any Estado class save
        when(estadoRepository.save(org.mockito.Mockito.any(Estado.class))).thenReturn(conId);

        EstadoResponseDTO resultado = estadoService.guardarEstado(request);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("CANCELADA", resultado.getNombre());

        verify(tipoEstadoRepository, times(1)).findById(1L);
        verify(estadoRepository, times(1)).save(org.mockito.Mockito.any(Estado.class));
    }
}
