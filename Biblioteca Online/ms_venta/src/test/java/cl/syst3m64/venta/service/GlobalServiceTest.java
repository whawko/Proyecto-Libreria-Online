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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.feign.EstadoFeignClient;
import cl.syst3m64.venta.feign.UsuarioFeignClient;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de GlobalService")
public class GlobalServiceTest {

    // crear mocks de los repositorios y feign clients
    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleRepository detalleRepository;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    // crear una instancia REAL del GlobalService con los mocks inyectados
    @InjectMocks
    private GlobalService globalService;

    // Variables para datos de pruebas reutilizables entre TEST
    private Venta ventaEjemplo;
    private VentaRequestDTO dtoRequest;

    // datos iniciales antes de cada test
    @BeforeEach
    void setUp() {
        ventaEjemplo = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        dtoRequest = new VentaRequestDTO("2024-01-15", new BigDecimal("50000"), 1L, 2L);
    }

    // TEST UNIT

    @Test
    @DisplayName("traerTodasLasVentas() retorna la lista de todas las ventas")
    void traerTodasLasVentas_debeRetornarListaDeVentas() {
        // el repositorio simulado retorna datos como si se conectara a MySQL
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEjemplo));

        // llamar a la funcion del servicio que quiero testear
        List<Venta> resultado = globalService.traerTodasLasVentas();

        // criterios de aceptacion
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2024-01-15", resultado.get(0).getFecha());

        // verificar que findAll() del repositorio fue llamado 1 vez
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("traerTodasLasVentas() debe retornar lista vacia cuando no hay ventas en MySQL")
    void traerTodasLasVentas_debeRetornarListaVacia_SiNoHayVentas() {
        // GIVEN
        when(ventaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Venta> resultado = globalService.traerTodasLasVentas();

        // criterios de aceptacion
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("traerVentasPorId() debe retornar Optional con VentaResponseDTO cuando existe")
    void traerVentasPorId_debeRetornarVenta_cuandoExiste() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaEjemplo));

        Optional<VentaResponseDTO> resultado = globalService.traerVentasPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("2024-01-15", resultado.get().getFecha());

        verify(ventaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("traerVentasPorId() debe retornar Optional vacio cuando el ID no existe")
    void traerVentasPorId_debeRetornarVacio_cuandoNoExiste() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<VentaResponseDTO> resultado = globalService.traerVentasPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("crearVenta() debe guardar y retornar VentaResponseDTO con datos validos")
    void crearVenta_debeRetornarVentaGuardada() {
        // simular que los feign no lanzaran excepcion (estado y usuario validos)
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new Object());
        when(usuarioFeignClient.obtenerUsuarioPorId(2L)).thenReturn(new Object());
        when(ventaRepository.save(org.mockito.ArgumentMatchers.any(Venta.class))).thenReturn(ventaEjemplo);

        VentaResponseDTO resultado = globalService.crearVenta(dtoRequest);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("2024-01-15", resultado.getFecha());
        assertEquals(new BigDecimal("50000"), resultado.getTotal());

        verify(ventaRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Venta.class));
    }

    @Test
    @DisplayName("eliminarVenta() debe llamar deleteById del repositorio")
    void eliminarVenta_debeEliminarCorrectamente() {
        doNothing().when(ventaRepository).deleteById(1L);

        globalService.eliminarVenta(1L);

        verify(ventaRepository, times(1)).deleteById(1L);
    }
}
