package cl.syst3m64.direccion.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import cl.syst3m64.direccion.feign.EstadoFeignClient;
import cl.syst3m64.direccion.feign.UsuarioFeignClient;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.DireccionRepository;
import cl.syst3m64.direccion.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de GlobalService")
public class GlobalServiceTest {

    //crear un mock de cada repositorio
    //debido a que no tenemos comunicación con dicho elemento a causa del Test
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private ComunaRepository comunaRepository;
    @Mock
    private DireccionRepository direccionRepository;

    //crear un mock de cada feign client
    //debido a que no tenemos comunicación con los microservicios externos
    @Mock
    private UsuarioFeignClient usuarioFeignClient;
    @Mock
    private EstadoFeignClient estadoFeignClient;

    //crear una instancia REAL del GlobalService
    @InjectMocks
    private GlobalService globalService;

    // Variables para datos de pruebas reutilizables entre TEST
    private Region regionEjemplo;
    private Comuna comunaEjemplo;
    private Direccion direccionEjemplo;

    //datos iniciales para las Tests
    //antes de que inicien
    @BeforeEach
    void setUp(){
        regionEjemplo = new Region(1L, "Region Metropolitana");
        comunaEjemplo = new Comuna(1L, "Santiago", regionEjemplo);
        direccionEjemplo = new Direccion(1L, "Av. Libertador", 1234, 1L, comunaEjemplo, 1L);
    }

    // TEST UNIT - Region

    @Test
    @DisplayName("obtenerRegiones() retorna la lista de todas las regiones")
    void obtenerRegiones_debeRetornarListaDeRegiones(){
        when(regionRepository.findAll()).thenReturn(List.of(regionEjemplo));

        List<Region> resultado = globalService.obtenerRegiones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Region Metropolitana", resultado.get(0).getNombre());

        verify(regionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerRegionPorId() retorna la region cuando existe")
    void obtenerRegionPorId_debeRetornarRegion_cuandoExiste(){
        when(regionRepository.findById(1L)).thenReturn(Optional.of(regionEjemplo));

        Optional<Region> resultado = globalService.obtenerRegionPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Region Metropolitana", resultado.get().getNombre());
    }

    @Test
    @DisplayName("obtenerRegionPorId() retorna Optional vacio cuando no existe")
    void obtenerRegionPorId_debeRetornarVacio_cuandoNoExiste(){
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Region> resultado = globalService.obtenerRegionPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("crearRegion() debe retornar la region creada")
    void crearRegion_debeRetornarRegionCreada(){
        Region nueva = new Region(null, "Region de Valparaiso");
        Region guardada = new Region(2L, "Region de Valparaiso");
        when(regionRepository.save(nueva)).thenReturn(guardada);

        Region resultado = globalService.crearRegion(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(regionRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("actualizarRegion() debe modificar y retornar la region cuando existe")
    void actualizarRegion_debeRetornarRegionActualizada_cuandoExiste(){
        Region cambios = new Region(null, "Region Metropolitana de Santiago");
        when(regionRepository.findById(1L)).thenReturn(Optional.of(regionEjemplo));
        when(regionRepository.save(regionEjemplo)).thenReturn(regionEjemplo);

        Region resultado = globalService.actualizarRegion(1L, cambios);

        assertNotNull(resultado);
        assertEquals("Region Metropolitana de Santiago", resultado.getNombre());
        verify(regionRepository, times(1)).save(regionEjemplo);
    }

    @Test
    @DisplayName("actualizarRegion() debe lanzar excepcion cuando la region no existe")
    void actualizarRegion_debeLanzarExcepcion_cuandoNoExiste(){
        Region cambios = new Region(null, "Region Inexistente");
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> globalService.actualizarRegion(99L, cambios));
    }

    @Test
    @DisplayName("eliminarRegion() debe invocar deleteById() del repositorio")
    void eliminarRegion_debeInvocarDeleteByIdDelRepositorio(){
        globalService.eliminarRegion(1L);

        verify(regionRepository, times(1)).deleteById(1L);
    }

    // TEST UNIT - Comuna

    @Test
    @DisplayName("obtenerComunas() retorna la lista de todas las comunas")
    void obtenerComunas_debeRetornarListaDeComunas(){
        when(comunaRepository.findAll()).thenReturn(List.of(comunaEjemplo));

        List<Comuna> resultado = globalService.obtenerComunas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getNombre());

        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerComunaPorId() retorna la comuna cuando existe")
    void obtenerComunaPorId_debeRetornarComuna_cuandoExiste(){
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEjemplo));

        Optional<Comuna> resultado = globalService.obtenerComunaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Santiago", resultado.get().getNombre());
    }

    @Test
    @DisplayName("obtenerComunaPorId() retorna Optional vacio cuando no existe")
    void obtenerComunaPorId_debeRetornarVacio_cuandoNoExiste(){
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Comuna> resultado = globalService.obtenerComunaPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("crearComuna() debe retornar la comuna creada")
    void crearComuna_debeRetornarComunaCreada(){
        Comuna nueva = new Comuna(null, "Vina del Mar", regionEjemplo);
        Comuna guardada = new Comuna(2L, "Vina del Mar", regionEjemplo);
        when(comunaRepository.save(nueva)).thenReturn(guardada);

        Comuna resultado = globalService.crearComuna(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(comunaRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("actualizarComuna() debe modificar y retornar la comuna cuando existe")
    void actualizarComuna_debeRetornarComunaActualizada_cuandoExiste(){
        Comuna cambios = new Comuna(null, "Providencia", regionEjemplo);
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEjemplo));
        when(comunaRepository.save(comunaEjemplo)).thenReturn(comunaEjemplo);

        Comuna resultado = globalService.actualizarComuna(1L, cambios);

        assertNotNull(resultado);
        assertEquals("Providencia", resultado.getNombre());
        verify(comunaRepository, times(1)).save(comunaEjemplo);
    }

    @Test
    @DisplayName("actualizarComuna() debe lanzar excepcion cuando la comuna no existe")
    void actualizarComuna_debeLanzarExcepcion_cuandoNoExiste(){
        Comuna cambios = new Comuna(null, "Comuna Inexistente", regionEjemplo);
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> globalService.actualizarComuna(99L, cambios));
    }

    @Test
    @DisplayName("eliminarComuna() debe invocar deleteById() del repositorio")
    void eliminarComuna_debeInvocarDeleteByIdDelRepositorio(){
        globalService.eliminarComuna(1L);

        verify(comunaRepository, times(1)).deleteById(1L);
    }

    // TEST UNIT - Direccion

    @Test
    @DisplayName("obtenerDirecciones() retorna la lista de todas las direcciones")
    void obtenerDirecciones_debeRetornarListaDeDirecciones(){
        when(direccionRepository.findAll()).thenReturn(List.of(direccionEjemplo));

        List<Direccion> resultado = globalService.obtenerDirecciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Av. Libertador", resultado.get(0).getCalle());

        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerDireccionPorId() retorna la direccion cuando existe")
    void obtenerDireccionPorId_debeRetornarDireccion_cuandoExiste(){
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccionEjemplo));

        Optional<Direccion> resultado = globalService.obtenerDireccionPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Av. Libertador", resultado.get().getCalle());
    }

    @Test
    @DisplayName("obtenerDireccionPorId() retorna Optional vacio cuando no existe")
    void obtenerDireccionPorId_debeRetornarVacio_cuandoNoExiste(){
        when(direccionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Direccion> resultado = globalService.obtenerDireccionPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("crearDireccion() debe validar usuario y estado, y retornar la direccion creada")
    void crearDireccion_debeRetornarDireccionCreada_cuandoUsuarioYEstadoValidos(){
        Direccion nueva = new Direccion(null, "Calle Los Alamos", 567, 1L, comunaEjemplo, 1L);
        Direccion guardada = new Direccion(2L, "Calle Los Alamos", 567, 1L, comunaEjemplo, 1L);
        when(usuarioFeignClient.obtenerUsuarioPorId(1L)).thenReturn(new Object());
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new Object());
        when(direccionRepository.save(nueva)).thenReturn(guardada);

        Direccion resultado = globalService.crearDireccion(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(usuarioFeignClient, times(1)).obtenerUsuarioPorId(1L);
        verify(estadoFeignClient, times(1)).obtenerEstadoPorId(1L);
        verify(direccionRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("actualizarDireccion() debe modificar y retornar la direccion cuando existe")
    void actualizarDireccion_debeRetornarDireccionActualizada_cuandoExiste(){
        Direccion cambios = new Direccion(null, "Nueva Calle", 999, 1L, comunaEjemplo, 1L);
        when(usuarioFeignClient.obtenerUsuarioPorId(1L)).thenReturn(new Object());
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new Object());
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccionEjemplo));
        when(direccionRepository.save(direccionEjemplo)).thenReturn(direccionEjemplo);

        Direccion resultado = globalService.actualizarDireccion(1L, cambios);

        assertNotNull(resultado);
        assertEquals("Nueva Calle", resultado.getCalle());
        assertEquals(999, resultado.getNumero());
        verify(direccionRepository, times(1)).save(direccionEjemplo);
    }

    @Test
    @DisplayName("actualizarDireccion() debe lanzar excepcion cuando la direccion no existe")
    void actualizarDireccion_debeLanzarExcepcion_cuandoNoExiste(){
        Direccion cambios = new Direccion(null, "Nueva Calle", 999, 1L, comunaEjemplo, 1L);
        when(usuarioFeignClient.obtenerUsuarioPorId(1L)).thenReturn(new Object());
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new Object());
        when(direccionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> globalService.actualizarDireccion(99L, cambios));
    }

    @Test
    @DisplayName("eliminarDireccion() debe invocar deleteById() del repositorio")
    void eliminarDireccion_debeInvocarDeleteByIdDelRepositorio(){
        globalService.eliminarDireccion(1L);

        verify(direccionRepository, times(1)).deleteById(1L);
    }

}
