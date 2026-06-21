package cl.syst3m64.envio.service;

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

import cl.syst3m64.envio.dto.DireccionResponseDTO;
import cl.syst3m64.envio.dto.EnvioRequestDTO;
import cl.syst3m64.envio.dto.EnvioResponseDTO;
import cl.syst3m64.envio.dto.VentaResponseDTO;
import cl.syst3m64.envio.client.DireccionFeignClient;
import cl.syst3m64.envio.client.VentaFeignClient;
import cl.syst3m64.envio.model.Envio;
import cl.syst3m64.envio.repository.EnvioRepository;
import cl.syst3m64.envio.service.impl.EnvioServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de EnvioService")
public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private VentaFeignClient ventaFeignClient;

    @Mock
    private DireccionFeignClient direccionFeignClient;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envioEjemplo;
    private VentaResponseDTO ventaPagada;
    private VentaResponseDTO ventaNoPagada;
    private DireccionResponseDTO direccionEjemplo;

    @BeforeEach
    void setUp() {
        envioEjemplo = new Envio(1L, 2L, 5L, "TRACK-12345", "FedEx", "2026-06-21", 1L);
        ventaPagada = new VentaResponseDTO(2L, new java.math.BigDecimal("15000"), 3L); // 3L is PAGADA
        ventaNoPagada = new VentaResponseDTO(2L, new java.math.BigDecimal("15000"), 1L); // 1L is ACTIVO (not paid)
        direccionEjemplo = new DireccionResponseDTO(5L, "Av. Siempre Viva", "742", "Springfield", "Springfield");
    }

    @Test
    @DisplayName("obtenerEnvioPorId() retorna el envio si existe")
    void obtenerEnvioPorId_debeRetornarEnvio() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envioEjemplo));

        Optional<EnvioResponseDTO> resultado = envioService.obtenerEnvioPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("TRACK-12345", resultado.get().getNumeroSeguimiento());
    }

    @Test
    @DisplayName("registrarEnvio() guarda el envio exitosamente si la venta está pagada")
    void registrarEnvio_debeRegistrarEnvio_cuandoVentaPagada() {
        EnvioRequestDTO request = new EnvioRequestDTO(2L, 5L, "TRACK-12345", "FedEx", "2026-06-21");
        
        when(ventaFeignClient.obtenerVentaPorId(2L)).thenReturn(ventaPagada);
        when(direccionFeignClient.obtenerDireccionPorId(5L)).thenReturn(direccionEjemplo);
        when(envioRepository.findByIdVenta(2L)).thenReturn(Optional.empty());
        when(envioRepository.save(any(Envio.class))).thenReturn(envioEjemplo);

        EnvioResponseDTO resultado = envioService.registrarEnvio(request);

        assertNotNull(resultado);
        assertEquals("TRACK-12345", resultado.getNumeroSeguimiento());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("registrarEnvio() lanza excepcion si la venta no está pagada")
    void registrarEnvio_debeLanzarExcepcion_cuandoVentaNoPagada() {
        EnvioRequestDTO request = new EnvioRequestDTO(2L, 5L, "TRACK-12345", "FedEx", "2026-06-21");
        
        when(ventaFeignClient.obtenerVentaPorId(2L)).thenReturn(ventaNoPagada);

        assertThrows(IllegalArgumentException.class, () -> {
            envioService.registrarEnvio(request);
        });
    }
}
