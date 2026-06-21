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
import cl.syst3m64.estado.dto.TipoEstadoRequestDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import cl.syst3m64.estado.service.impl.TipoEstadoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de TipoEstadoService")
public class TipoEstadoServiceTest {

    @Mock
    private TipoEstadoRepository tipoEstadoRepository;

    @InjectMocks
    private TipoEstadoServiceImpl tipoEstadoService;

    private TipoEstado tipoEstadoEjemplo;

    @BeforeEach
    void setUp() {
        tipoEstadoEjemplo = new TipoEstado(1L, "VENTA");
    }

    @Test
    @DisplayName("obtenerTipoEstados() retorna la lista de todos los tipos de estado en DTOs")
    void obtenerTipoEstados_debeRetornarListaDeTipos() {
        when(tipoEstadoRepository.findAll()).thenReturn(List.of(tipoEstadoEjemplo));

        List<TipoEstadoResponseDTO> resultado = tipoEstadoService.obtenerTipoEstados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("VENTA", resultado.get(0).getNombre());

        verify(tipoEstadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTipoEstadosPorId() retorna el DTO del tipo de estado cuando existe")
    void obtenerTipoEstadosPorId_debeRetornarTipo_cuandoExiste() {
        when(tipoEstadoRepository.findById(1L)).thenReturn(Optional.of(tipoEstadoEjemplo));

        Optional<TipoEstadoResponseDTO> resultado = tipoEstadoService.obtenerTipoEstadosPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("VENTA", resultado.get().getNombre());

        verify(tipoEstadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("guardarTipoEstado() retorna el DTO del tipo de estado guardado")
    void guardarTipoEstado_debeRetornarTipoGuardado() {
        TipoEstadoRequestDTO request = new TipoEstadoRequestDTO("ARRIENDO");
        TipoEstado conId = new TipoEstado(2L, "ARRIENDO");

        when(tipoEstadoRepository.save(org.mockito.Mockito.any(TipoEstado.class))).thenReturn(conId);

        TipoEstadoResponseDTO resultado = tipoEstadoService.guardarTipoEstado(request);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("ARRIENDO", resultado.getNombre());

        verify(tipoEstadoRepository, times(1)).save(org.mockito.Mockito.any(TipoEstado.class));
    }
}
