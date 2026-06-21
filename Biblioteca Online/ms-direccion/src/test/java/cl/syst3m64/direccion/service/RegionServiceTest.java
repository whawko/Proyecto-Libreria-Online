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

import cl.syst3m64.direccion.dto.RegionRequestDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.RegionRepository;
import cl.syst3m64.direccion.service.impl.RegionServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de RegionService")
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImpl regionService;

    private Region regionEjemplo;

    @BeforeEach
    void setUp(){
        regionEjemplo = new Region(1L, "Region Metropolitana");
    }

    @Test
    @DisplayName("obtenerRegiones() retorna la lista de todas las regiones")
    void obtenerRegiones_debeRetornarListaDeRegiones(){
        when(regionRepository.findAll()).thenReturn(List.of(regionEjemplo));

        List<RegionResponseDTO> resultado = regionService.obtenerRegiones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Region Metropolitana", resultado.get(0).getNombre());

        verify(regionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerRegionPorId() retorna la region cuando existe")
    void obtenerRegionPorId_debeRetornarRegion_cuandoExiste(){
        when(regionRepository.findById(1L)).thenReturn(Optional.of(regionEjemplo));

        Optional<RegionResponseDTO> resultado = regionService.obtenerRegionPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Region Metropolitana", resultado.get().getNombre());
    }

    @Test
    @DisplayName("crearRegion() debe retornar la region creada")
    void crearRegion_debeRetornarRegionCreada(){
        RegionRequestDTO nueva = new RegionRequestDTO("Region de Valparaiso");
        Region guardada = new Region(2L, "Region de Valparaiso");
        when(regionRepository.save(any(Region.class))).thenReturn(guardada);

        RegionResponseDTO resultado = regionService.crearRegion(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Region de Valparaiso", resultado.getNombre());
        verify(regionRepository, times(1)).save(any(Region.class));
    }
}
