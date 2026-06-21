package cl.syst3m64.venta.service;

import java.math.BigDecimal;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.venta.dto.DetalleRequestDTO;
import cl.syst3m64.venta.dto.DetalleResponseDTO;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;
import cl.syst3m64.venta.service.impl.DetalleServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de DetalleService")
public class DetalleServiceTest {

    @Mock
    private DetalleRepository detalleRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private DetalleServiceImpl detalleService;

    private Venta ventaEjemplo;
    private Detalle detalleEjemplo;

    @BeforeEach
    void setUp() {
        ventaEjemplo = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        detalleEjemplo = new Detalle(1L, 2, new BigDecimal("10000"), ventaEjemplo, 3L);
    }

    @Test
    @DisplayName("traerTodosLosDetalles() retorna todos los detalles")
    void traerTodosLosDetalles_debeRetornarLista() {
        when(detalleRepository.findAll()).thenReturn(List.of(detalleEjemplo));

        List<DetalleResponseDTO> resultado = detalleService.traerTodosLosDetalles();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getCantidad());

        verify(detalleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("crearDetalle() guarda el detalle correctamente")
    void crearDetalle_debeGuardarDetalle() {
        DetalleRequestDTO request = new DetalleRequestDTO(2, new BigDecimal("10000"), 3L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEjemplo));
        when(detalleRepository.save(any(Detalle.class))).thenReturn(detalleEjemplo);

        DetalleResponseDTO resultado = detalleService.crearDetalle(request, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdVenta());
        verify(detalleRepository, times(1)).save(any(Detalle.class));
    }
}
