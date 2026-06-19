package cl.syst3m64.libros.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import cl.syst3m64.libros.model.Libro;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de libros en memoria")
public class LibroRepositoryTest {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Libro libroUno;
    private Libro libroDos;

    // insertar datos antes de cada test
    @BeforeEach
    void setUp() {
        libroUno = entityManager.persistAndFlush(
            new Libro(null, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L)
        );

        libroDos = entityManager.persistAndFlush(
            new Libro(null, "Cien Anos de Soledad", "Realismo magico", "Garcia Marquez", "978-5678", 12000.0f, "1967", "Sudamericana", 1L, 2L)
        );
    }

    // TEST para findAll() -- Heredado del JpaRepository
    @Test
    @DisplayName("findAll() debe retornar todos los libros insertados")
    void findAll_debeRetornarTodosLosLibros() {
        List<Libro> libros = libroRepository.findAll();

        assertNotNull(libros);
        assertEquals(2, libros.size());
    }

    // TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con el libro cuando existe")
    void findById_debeRetornarLibro_cuandoExiste() {
        Optional<Libro> resultado = libroRepository.findById(libroUno.getId());

        assertTrue(resultado.isPresent());
        assertEquals("El Quijote", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Libro> resultado = libroRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    // TEST para save()
    @Test
    @DisplayName("save() debe persistir un nuevo libro y asignarle un ID")
    void save_debeGuardarLibro_yAsignarId() {
        Libro nuevoLibro = new Libro(null, "Don Juan Tenorio", "Drama romantico", "Zorrilla", "978-9999", 8000.0f, "1844", "Espasa", 2L, 1L);
        Libro guardado = libroRepository.save(nuevoLibro);

        assertNotNull(guardado.getId());
        assertEquals("Don Juan Tenorio", guardado.getTitulo());
    }

    // TEST para deleteById()
    @Test
    @DisplayName("deleteById() debe eliminar el libro correctamente")
    void deleteById_debeEliminarLibro_cuandoExiste() {
        libroRepository.deleteById(libroUno.getId());

        Optional<Libro> resultado = libroRepository.findById(libroUno.getId());

        assertFalse(resultado.isPresent());
    }

    // TEST para findByIdCategoria() -- metodo personalizado del repositorio
    @Test
    @DisplayName("findByIdCategoria() debe retornar los libros de una categoria")
    void findByIdCategoria_debeRetornarLibrosDeLaCategoria() {
        List<Libro> resultado = libroRepository.findByIdCategoria(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    // TEST para buscarPorTituloParecido() -- metodo personalizado con @Query
    @Test
    @DisplayName("buscarPorTituloParecido() debe retornar libros cuyo titulo coincida parcialmente")
    void buscarPorTituloParecido_debeRetornarLibrosCoincidentes() {
        List<Libro> resultado = libroRepository.buscarPorTituloParecido("quijote");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Quijote", resultado.get(0).getTitulo());
    }
}
