package cl.syst3m64.carrito.service;

import java.math.BigDecimal;
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

import cl.syst3m64.carrito.dto.CarritoRequestDTO;
import cl.syst3m64.carrito.dto.CarritoResponseDTO;
import cl.syst3m64.carrito.dto.LibroResponseDTO;
import cl.syst3m64.carrito.dto.UsuarioDTO;
import cl.syst3m64.carrito.client.LibroFeignClient;
import cl.syst3m64.carrito.client.UsuarioFeignClient;
import cl.syst3m64.carrito.model.Carrito;
import cl.syst3m64.carrito.repository.CarritoRepository;
import cl.syst3m64.carrito.service.impl.CarritoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CarritoService")
public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @Mock
    private LibroFeignClient libroFeignClient;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito carritoEjemplo;
    private LibroResponseDTO libroEjemplo;

    @BeforeEach
    void setUp() {
        carritoEjemplo = new Carrito(1L, 1L, 2L, 3, new BigDecimal("1000"), new BigDecimal("3000"));
        libroEjemplo = new LibroResponseDTO(2L, "El Quijote", 1000.0f);
    }

    @Test
    @DisplayName("obtenerCarritoPorUsuario() retorna items del carrito")
    void obtenerCarritoPorUsuario_debeRetornarLista() {
        when(carritoRepository.findByIdUsuario(1L)).thenReturn(List.of(carritoEjemplo));

        List<CarritoResponseDTO> resultado = carritoService.obtenerCarritoPorUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("3000"), resultado.get(0).getSubtotal());
    }

    @Test
    @DisplayName("agregarAlCarrito() agrega un item nuevo correctamente")
    void agregarAlCarrito_debeAgregarItemNuevo() {
        CarritoRequestDTO request = new CarritoRequestDTO(1L, 2L, 3);
        when(usuarioFeignClient.obtenerUsuarioPorId(1L)).thenReturn(new UsuarioDTO());
        when(libroFeignClient.obtenerLibroPorId(2L)).thenReturn(libroEjemplo);
        when(carritoRepository.findByIdUsuarioAndIdLibro(1L, 2L)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenReturn(carritoEjemplo);

        CarritoResponseDTO resultado = carritoService.agregarAlCarrito(request);

        assertNotNull(resultado);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    @DisplayName("actualizarCantidad() cambia la cantidad correctamente")
    void actualizarCantidad_debeActualizarCantidad() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carritoEjemplo));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CarritoResponseDTO resultado = carritoService.actualizarCantidad(1L, 5);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
        assertEquals(new BigDecimal("5000"), resultado.getSubtotal());
    }

    @Test
    @DisplayName("actualizarCantidad() lanza excepcion si cantidad < 1")
    void actualizarCantidad_debeLanzarExcepcion_cuandoCantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            carritoService.actualizarCantidad(1L, 0);
        });
    }
}
