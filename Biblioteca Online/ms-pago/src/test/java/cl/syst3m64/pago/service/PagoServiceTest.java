package cl.syst3m64.pago.service;

import java.math.BigDecimal;
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

import cl.syst3m64.pago.dto.PagoRequestDTO;
import cl.syst3m64.pago.dto.PagoResponseDTO;
import cl.syst3m64.pago.dto.VentaResponseDTO;
import cl.syst3m64.pago.client.VentaFeignClient;
import cl.syst3m64.pago.model.Pago;
import cl.syst3m64.pago.repository.PagoRepository;
import cl.syst3m64.pago.service.impl.PagoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de PagoService")
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private VentaFeignClient ventaFeignClient;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pagoEjemplo;
    private VentaResponseDTO ventaEjemplo;

    @BeforeEach
    void setUp() {
        pagoEjemplo = new Pago(1L, 2L, new BigDecimal("15000"), "TARJETA", "TX-12345", "2026-06-21", 1L);
        ventaEjemplo = new VentaResponseDTO(2L, new BigDecimal("15000"), 1L);
    }

    @Test
    @DisplayName("obtenerPagoPorId() retorna el pago si existe")
    void obtenerPagoPorId_debeRetornarPago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pagoEjemplo));

        Optional<PagoResponseDTO> resultado = pagoService.obtenerPagoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("TX-12345", resultado.get().getTransaccionId());
    }

    @Test
    @DisplayName("registrarPago() guarda el pago y actualiza la venta exitosamente")
    void registrarPago_debeRegistrarPago() {
        PagoRequestDTO request = new PagoRequestDTO(2L, new BigDecimal("15000"), "TARJETA", "TX-12345", "2026-06-21");
        
        when(ventaFeignClient.obtenerVentaPorId(2L)).thenReturn(ventaEjemplo);
        when(pagoRepository.findByIdVenta(2L)).thenReturn(Optional.empty());
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoEjemplo);
        when(ventaFeignClient.actualizarEstadoVenta(2L, 3L)).thenReturn(ventaEjemplo);

        PagoResponseDTO resultado = pagoService.registrarPago(request);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("15000"), resultado.getMonto());
        verify(pagoRepository, times(1)).save(any(Pago.class));
        verify(ventaFeignClient, times(1)).actualizarEstadoVenta(2L, 3L);
    }

    @Test
    @DisplayName("registrarPago() lanza excepcion si el monto no coincide")
    void registrarPago_debeLanzarExcepcion_cuandoMontoNoCoincide() {
        PagoRequestDTO request = new PagoRequestDTO(2L, new BigDecimal("20000"), "TARJETA", "TX-12345", "2026-06-21");
        
        when(ventaFeignClient.obtenerVentaPorId(2L)).thenReturn(ventaEjemplo);

        assertThrows(IllegalArgumentException.class, () -> {
            pagoService.registrarPago(request);
        });
    }
}
