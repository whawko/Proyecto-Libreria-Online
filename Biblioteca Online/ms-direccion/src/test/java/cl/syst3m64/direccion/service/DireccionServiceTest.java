package cl.syst3m64.direccion.service;

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

import cl.syst3m64.direccion.dto.DireccionRequestDTO;
import cl.syst3m64.direccion.dto.DireccionResponseDTO;
import cl.syst3m64.direccion.dto.EstadoDTO;
import cl.syst3m64.direccion.dto.UsuarioDTO;
import cl.syst3m64.direccion.client.EstadoFeignClient;
import cl.syst3m64.direccion.client.UsuarioFeignClient;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.DireccionRepository;
import cl.syst3m64.direccion.service.impl.DireccionServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de DireccionService")
public class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private UsuarioFeignClient usuarioFeignClient;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    @InjectMocks
    private DireccionServiceImpl direccionService;

    private Region regionEjemplo;
    private Comuna comunaEjemplo;
    private Direccion direccionEjemplo;

    @BeforeEach
    void setUp(){
        regionEjemplo = new Region(1L, "Region Metropolitana");
        comunaEjemplo = new Comuna(1L, "Santiago", regionEjemplo);
        direccionEjemplo = new Direccion(1L, "Av. Libertador", 1234, 1L, comunaEjemplo, 1L);
    }

    @Test
    @DisplayName("obtenerDirecciones() retorna la lista de todas las direcciones")
    void obtenerDirecciones_debeRetornarListaDeDirecciones(){
        when(direccionRepository.findAll()).thenReturn(List.of(direccionEjemplo));

        List<DireccionResponseDTO> resultado = direccionService.obtenerDirecciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Av. Libertador", resultado.get(0).getCalle());

        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerDireccionPorId() retorna la direccion cuando existe")
    void obtenerDireccionPorId_debeRetornarDireccion_cuandoExiste(){
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccionEjemplo));

        Optional<DireccionResponseDTO> resultado = direccionService.obtenerDireccionPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Av. Libertador", resultado.get().getCalle());
    }

    @Test
    @DisplayName("crearDireccion() debe retornar la direccion creada")
    void crearDireccion_debeRetornarDireccionCreada(){
        DireccionRequestDTO nueva = new DireccionRequestDTO("Av. Libertador", 1234, 1L, 1L, 1L);
        when(usuarioFeignClient.obtenerUsuarioPorId(1L)).thenReturn(new UsuarioDTO());
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoDTO());
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEjemplo));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(direccionEjemplo);

        DireccionResponseDTO resultado = direccionService.crearDireccion(nueva);

        assertNotNull(resultado);
        assertEquals("Av. Libertador", resultado.getCalle());
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }
}
