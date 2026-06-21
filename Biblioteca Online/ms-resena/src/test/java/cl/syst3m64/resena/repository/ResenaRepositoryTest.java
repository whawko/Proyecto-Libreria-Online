package cl.syst3m64.resena.repository;

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

import cl.syst3m64.resena.model.Resena;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de resenas en memoria")
public class ResenaRepositoryTest {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Resena resena1;
    private Resena resena2;

    // insertar datos antes de cada test
    @BeforeEach
    void setUp() {
        resena1 = entityManager.persistAndFlush(
                new Resena(null, 1L, 2L, "Gran libro",
                        "Un clasico imprescindible de la literatura", 5, null, null));
        resena2 = entityManager.persistAndFlush(
                new Resena(null, 2L, 3L, "Lectura recomendada",
                        "Me gusto mucho esta novela, la recomiendo", 4, null, null));
    }

    // TEST para findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las resenas insertadas")
    void findAll_debeRetornarTodasLasResenas() {
        List<Resena> resenas = resenaRepository.findAll();

        assertNotNull(resenas);
        assertEquals(2, resenas.size());
    }

    // TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la resena cuando existe")
    void findById_debeRetornarResena_cuandoExiste() {
        Optional<Resena> resultado = resenaRepository.findById(resena1.getIdResena());

        assertTrue(resultado.isPresent());
        assertEquals("Gran libro", resultado.get().getTitulo());
        assertEquals(5, resultado.get().getCalificacion());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Resena> resultado = resenaRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    // TEST para findByIdLibroAndEstado() -- metodo personalizado
    @Test
    @DisplayName("findByIdLibroAndEstado() debe retornar resenas activas de un libro")
    void findByIdLibroAndEstado_debeRetornarResenasPorLibro() {
        List<Resena> resultado = resenaRepository.findByIdLibroAndEstado(1L, "ACTIVO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdLibro());
    }

    // TEST para findByIdUsuarioAndEstado() -- metodo personalizado
    @Test
    @DisplayName("findByIdUsuarioAndEstado() debe retornar resenas activas de un usuario")
    void findByIdUsuarioAndEstado_debeRetornarResenasPorUsuario() {
        List<Resena> resultado = resenaRepository.findByIdUsuarioAndEstado(2L, "ACTIVO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getIdUsuario());
    }

    // TEST para existsByIdLibroAndIdUsuario() -- metodo personalizado
    @Test
    @DisplayName("existsByIdLibroAndIdUsuario() debe retornar true cuando ya existe la resena")
    void existsByIdLibroAndIdUsuario_debeRetornarTrue_cuandoExiste() {
        boolean existe = resenaRepository.existsByIdLibroAndIdUsuario(1L, 2L);

        assertTrue(existe);
    }

    @Test
    @DisplayName("existsByIdLibroAndIdUsuario() debe retornar false cuando no existe la resena")
    void existsByIdLibroAndIdUsuario_debeRetornarFalse_cuandoNoExiste() {
        boolean existe = resenaRepository.existsByIdLibroAndIdUsuario(99L, 99L);

        assertFalse(existe);
    }
}

