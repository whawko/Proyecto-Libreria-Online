package cl.syst3m64.venta.service;

import java.math.BigDecimal;
import java.util.Collections;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.venta.dto.CarritoItemDTO;
import cl.syst3m64.venta.dto.EstadoDTO;
import cl.syst3m64.venta.dto.UsuarioDTO;
import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.client.CarritoFeignClient;
import cl.syst3m64.venta.client.EstadoFeignClient;
import cl.syst3m64.venta.client.UsuarioFeignClient;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;
import cl.syst3m64.venta.service.impl.VentaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de VentaService")
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @Mock
    private CarritoFeignClient carritoFeignClient;

    @Mock
    private DetalleRepository detalleRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta ventaEjemplo;
    private VentaRequestDTO dtoRequest;

    @BeforeEach
    void setUp() {
        ventaEjemplo = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        dtoRequest = new VentaRequestDTO("2024-01-15", new BigDecimal("50000"), 1L, 2L);
    }

    @Test
    @DisplayName("traerTodasLasVentas() retorna la lista de todas las ventas")
    void traerTodasLasVentas_debeRetornarListaDeVentas() {
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEjemplo));

        List<VentaResponseDTO> resultado = ventaService.traerTodasLasVentas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2024-01-15", resultado.get(0).getFecha());

        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("traerVentasPorId() debe retornar Optional con VentaResponseDTO cuando existe")
    void traerVentasPorId_debeRetornarVenta_cuandoExiste() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEjemplo));

        Optional<VentaResponseDTO> resultado = ventaService.traerVentasPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(new BigDecimal("50000"), resultado.get().getTotal());
    }

    @Test
    @DisplayName("crearVentaDesdeCarrito() genera venta y vacía el carrito exitosamente")
    void crearVentaDesdeCarrito_debeGenerarVentaYVaciarCarrito() {
        Long idUsuario = 2L;
        CarritoItemDTO item = new CarritoItemDTO(1L, idUsuario, 3L, 2, new BigDecimal("25000"), new BigDecimal("50000"));

        when(usuarioFeignClient.obtenerUsuarioPorId(idUsuario)).thenReturn(new UsuarioDTO());
        when(carritoFeignClient.obtenerCarritoPorUsuario(idUsuario)).thenReturn(List.of(item));
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaEjemplo);
        when(detalleRepository.save(any(Detalle.class))).thenReturn(new Detalle());
        doNothing().when(carritoFeignClient).vaciarCarrito(idUsuario);

        VentaResponseDTO resultado = ventaService.crearVentaDesdeCarrito(idUsuario);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("50000"), resultado.getTotal());
        assertEquals(1L, resultado.getIdEstado());

        verify(usuarioFeignClient, times(1)).obtenerUsuarioPorId(idUsuario);
        verify(carritoFeignClient, times(1)).obtenerCarritoPorUsuario(idUsuario);
        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(detalleRepository, times(1)).save(any(Detalle.class));
        verify(carritoFeignClient, times(1)).vaciarCarrito(idUsuario);
    }

    @Test
    @DisplayName("crearVentaDesdeCarrito() lanza excepción si el carrito está vacío")
    void crearVentaDesdeCarrito_debeLanzarExcepcion_cuandoCarritoVacio() {
        Long idUsuario = 2L;

        when(usuarioFeignClient.obtenerUsuarioPorId(idUsuario)).thenReturn(new UsuarioDTO());
        when(carritoFeignClient.obtenerCarritoPorUsuario(idUsuario)).thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> {
            ventaService.crearVentaDesdeCarrito(idUsuario);
        });

        verify(carritoFeignClient, times(1)).obtenerCarritoPorUsuario(idUsuario);
    }

    @Test
    @DisplayName("actualizarEstadoVenta() cambia el estado de la venta correctamente")
    void actualizarEstadoVenta_debeCambiarEstadoVenta() {
        Long idVenta = 1L;
        Long nuevoEstado = 3L;

        when(estadoFeignClient.obtenerEstadoPorId(nuevoEstado)).thenReturn(new EstadoDTO());
        when(ventaRepository.findById(idVenta)).thenReturn(Optional.of(ventaEjemplo));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VentaResponseDTO resultado = ventaService.actualizarEstadoVenta(idVenta, nuevoEstado);

        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getIdEstado());

        verify(ventaRepository, times(1)).findById(idVenta);
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }
}
