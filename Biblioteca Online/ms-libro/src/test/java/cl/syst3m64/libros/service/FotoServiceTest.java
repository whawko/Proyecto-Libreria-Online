package cl.syst3m64.libros.service;

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

import cl.syst3m64.libros.dto.FotoRequestDTO;
import cl.syst3m64.libros.dto.FotoResponseDTO;
import cl.syst3m64.libros.model.Foto;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.FotoRepository;
import cl.syst3m64.libros.repository.LibroRepository;
import cl.syst3m64.libros.service.impl.FotoServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de FotoService")
public class FotoServiceTest {

    @Mock
    private FotoRepository fotoRepository;

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private FotoServiceImpl fotoService;

    private Libro libroEjemplo;
    private Foto fotoEjemplo;

    @BeforeEach
    void setUp() {
        libroEjemplo = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        fotoEjemplo = new Foto(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", libroEjemplo);
    }

    @Test
    @DisplayName("obtenerFotos() retorna la lista de todas las fotos")
    void obtenerFotos_debeRetornarLista() {
        when(fotoRepository.findAll()).thenReturn(List.of(fotoEjemplo));

        List<FotoResponseDTO> resultado = fotoService.obtenerFotos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("http://imagen.com/foto1.jpg", resultado.get(0).getUrl());

        verify(fotoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerFotos() debe retornar lista vacia cuando no hay fotos")
    void obtenerFotos_debeRetornarListaVacia_siNoHayFotos() {
        when(fotoRepository.findAll()).thenReturn(List.of());

        List<FotoResponseDTO> resultado = fotoService.obtenerFotos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId() retorna la foto cuando existe")
    void obtenerPorId_debeRetornarFoto_cuandoExiste() {
        when(fotoRepository.findById(1L)).thenReturn(Optional.of(fotoEjemplo));

        Optional<FotoResponseDTO> resultado = fotoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("foto1", resultado.get().getNombre());

        verify(fotoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() retorna Optional vacio cuando el ID no existe")
    void obtenerPorId_debeRetornarVacio_cuandoNoExiste() {
        when(fotoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<FotoResponseDTO> resultado = fotoService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("guardarFoto() asocia el libro y retorna la foto guardada")
    void guardarFoto_debeRetornarFotoGuardada() {
        FotoRequestDTO nueva = new FotoRequestDTO("http://imagen.com/foto2.jpg", "foto2", "NO");
        Foto guardada = new Foto(2L, "http://imagen.com/foto2.jpg", "foto2", "NO", libroEjemplo);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroEjemplo));
        when(fotoRepository.save(any(Foto.class))).thenReturn(guardada);

        FotoResponseDTO resultado = fotoService.guardarFoto(nueva, 1L);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("foto2", resultado.getNombre());
        assertEquals(1L, resultado.getIdLibro());

        verify(fotoRepository, times(1)).save(any(Foto.class));
    }
}
