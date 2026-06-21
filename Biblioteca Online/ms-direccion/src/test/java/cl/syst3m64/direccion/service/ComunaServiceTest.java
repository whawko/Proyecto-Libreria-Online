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

import cl.syst3m64.direccion.dto.ComunaRequestDTO;
import cl.syst3m64.direccion.dto.ComunaResponseDTO;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.RegionRepository;
import cl.syst3m64.direccion.service.impl.ComunaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de ComunaService")
public class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private ComunaServiceImpl comunaService;

    private Region regionEjemplo;
    private Comuna comunaEjemplo;

    @BeforeEach
    void setUp(){
        regionEjemplo = new Region(1L, "Region Metropolitana");
        comunaEjemplo = new Comuna(1L, "Santiago", regionEjemplo);
    }

    @Test
    @DisplayName("obtenerComunas() retorna la lista de todas las comunas")
    void obtenerComunas_debeRetornarListaDeComunas(){
        when(comunaRepository.findAll()).thenReturn(List.of(comunaEjemplo));

        List<ComunaResponseDTO> resultado = comunaService.obtenerComunas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Santiago", resultado.get(0).getNombre());

        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerComunaPorId() retorna la comuna cuando existe")
    void obtenerComunaPorId_debeRetornarComuna_cuandoExiste(){
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaEjemplo));

        Optional<ComunaResponseDTO> resultado = comunaService.obtenerComunaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Santiago", resultado.get().getNombre());
    }

    @Test
    @DisplayName("crearComuna() debe retornar la comuna creada")
    void crearComuna_debeRetornarComunaCreada(){
        ComunaRequestDTO nueva = new ComunaRequestDTO("Providencia", 1L);
        Comuna guardada = new Comuna(2L, "Providencia", regionEjemplo);
        
        when(regionRepository.findById(1L)).thenReturn(Optional.of(regionEjemplo));
        when(comunaRepository.save(any(Comuna.class))).thenReturn(guardada);

        ComunaResponseDTO resultado = comunaService.crearComuna(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Providencia", resultado.getNombre());
        verify(regionRepository, times(1)).findById(1L);
        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }
}
