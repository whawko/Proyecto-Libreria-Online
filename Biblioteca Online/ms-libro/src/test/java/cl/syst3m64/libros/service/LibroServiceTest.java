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

import cl.syst3m64.libros.dto.LibroRequestDTO;
import cl.syst3m64.libros.dto.LibroResponseDTO;
import cl.syst3m64.libros.dto.CategoriaDTO;
import cl.syst3m64.libros.dto.EstadoDTO;
import cl.syst3m64.libros.client.CategoriaFeignClient;
import cl.syst3m64.libros.client.EstadoFeignClient;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.LibroRepository;
import cl.syst3m64.libros.service.impl.LibroServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de LibroService")
public class LibroServiceTest {

    // crear mocks ficticios del repositorio y de los clientes feign
    // debido a que no tenemos comunicacion con MySQL ni con los otros microservicios a causa del Test
    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CategoriaFeignClient categoriaFeignClient;

    @Mock
    private EstadoFeignClient estadoFeignClient;

    // crear una instancia REAL del LibroService con los mocks inyectados
    @InjectMocks
    private LibroServiceImpl libroService;


    // Variables para datos de pruebas reutilizables entre TEST
    private Libro libroEjemplo;
    private LibroRequestDTO requestEjemplo;

    // datos iniciales antes de que inicien los tests
    @BeforeEach
    void setUp() {
        libroEjemplo = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        requestEjemplo = new LibroRequestDTO("El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
    }

    // TEST UNIT

    @Test
    @DisplayName("obtenerLibros() retorna la lista de todos los libros")
    void obtenerLibros_debeRetornarLista() {
        // el repositorio simulado retorna datos como si se estuviese conectando a MySQL
        when(libroRepository.findAll()).thenReturn(List.of(libroEjemplo));

        List<LibroResponseDTO> resultado = libroService.obtenerLibros();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Quijote", resultado.get(0).getTitulo());

        // verificar que el findAll() del repositorio fue llamado 1 vez
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerLibros() debe retornar lista vacia cuando no hay libros en MySQL")
    void obtenerLibros_debeRetornarListaVacia_siNoHayLibros() {
        when(libroRepository.findAll()).thenReturn(List.of());

        List<LibroResponseDTO> resultado = libroService.obtenerLibros();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId() retorna el libro cuando existe")
    void obtenerPorId_debeRetornarLibro_cuandoExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroEjemplo));

        Optional<LibroResponseDTO> resultado = libroService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("El Quijote", resultado.get().getTitulo());
        assertEquals("Cervantes", resultado.get().getAutor());

        verify(libroRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() retorna Optional vacio cuando el ID no existe")
    void obtenerPorId_debeRetornarVacio_cuandoNoExiste() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<LibroResponseDTO> resultado = libroService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("guardarLibro() retorna el libro guardado con ID asignado")
    void guardarLibro_debeRetornarLibroGuardado() {
        // simular que los feign retornan objetos validos (categoria y estado existen)
        when(categoriaFeignClient.obtenerCategoriaPorId(1L)).thenReturn(new CategoriaDTO());
        when(estadoFeignClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoDTO());
        when(libroRepository.save(any(Libro.class))).thenReturn(libroEjemplo);

        LibroResponseDTO resultado = libroService.guardarLibro(requestEjemplo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("El Quijote", resultado.getTitulo());

        verify(libroRepository, times(1)).save(any(Libro.class));
    }
}
