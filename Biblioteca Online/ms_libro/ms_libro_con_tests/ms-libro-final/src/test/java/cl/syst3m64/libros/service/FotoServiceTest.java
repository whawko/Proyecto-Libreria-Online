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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.syst3m64.libros.model.Foto;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.FotoRepository;
import cl.syst3m64.libros.repository.LibroRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de FotoService")
public class FotoServiceTest {

    // crear mocks ficticios de los repositorios
    // debido a que no tenemos comunicacion con MySQL a causa del Test
    @Mock
    private FotoRepository fotoRepository;

    @Mock
    private LibroRepository libroRepository;

    // crear una instancia REAL del FotoService con los mocks inyectados
    @InjectMocks
    private FotoService fotoService;

    // Variables para datos de pruebas reutilizables entre TEST
    private Libro libroEjemplo;
    private Foto fotoEjemplo;

    // datos iniciales antes de que inicien los tests
    @BeforeEach
    void setUp() {
        libroEjemplo = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        fotoEjemplo = new Foto(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", libroEjemplo);
    }

    // TEST UNIT

    @Test
    @DisplayName("obtenerFotos() retorna la lista de todas las fotos")
    void obtenerFotos_debeRetornarLista() {
        // el repositorio simulado retorna datos como si se estuviese conectando a MySQL
        when(fotoRepository.findAll()).thenReturn(List.of(fotoEjemplo));

        List<Foto> resultado = fotoService.obtenerFotos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("http://imagen.com/foto1.jpg", resultado.get(0).getUrl());

        // verificar que el findAll() del repositorio fue llamado 1 vez
        verify(fotoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerFotos() debe retornar lista vacia cuando no hay fotos en MySQL")
    void obtenerFotos_debeRetornarListaVacia_siNoHayFotos() {
        when(fotoRepository.findAll()).thenReturn(List.of());

        List<Foto> resultado = fotoService.obtenerFotos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId() retorna la foto cuando existe")
    void obtenerPorId_debeRetornarFoto_cuandoExiste() {
        when(fotoRepository.findById(1L)).thenReturn(Optional.of(fotoEjemplo));

        Optional<Foto> resultado = fotoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("foto1", resultado.get().getNombre());

        verify(fotoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() retorna Optional vacio cuando el ID no existe")
    void obtenerPorId_debeRetornarVacio_cuandoNoExiste() {
        when(fotoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Foto> resultado = fotoService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("guardarFoto() asocia el libro y retorna la foto guardada")
    void guardarFoto_debeRetornarFotoGuardada() {
        Foto sinLibro = new Foto(null, "http://imagen.com/foto2.jpg", "foto2", "NO", null);
        Foto conLibro = new Foto(2L, "http://imagen.com/foto2.jpg", "foto2", "NO", libroEjemplo);

        // simular que el libro existe en el repositorio
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroEjemplo));
        when(fotoRepository.save(sinLibro)).thenReturn(conLibro);

        Foto resultado = fotoService.guardarFoto(sinLibro, 1L);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("foto2", resultado.getNombre());
        assertEquals(libroEjemplo, resultado.getLibro());

        verify(fotoRepository, times(1)).save(sinLibro);
    }
}
