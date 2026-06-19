package cl.syst3m64.categorias.repository;

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

import cl.syst3m64.categorias.model.Categoria;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de categorias en memoria")
public class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TestEntityManager entityManager;

    //Variables para datos insertados en memoria antes de cada test
    private Categoria electronica;
    private Categoria hogar;

    //insertar datos antes de cada test
    @BeforeEach
    void setUp(){
        electronica = entityManager.persistAndFlush(
            new Categoria(null, "Electronica", "Articulos electronicos en general")
        );
        hogar = entityManager.persistAndFlush(
            new Categoria(null, "Hogar", "Articulos para el hogar")
        );
    }

    //TEST para el findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las categorias insertadas")
    void findAll_debeRetornarTodasLasCategorias(){
        //logica de negocios que debe ejecutar la prueba
        List<Categoria> categorias = categoriaRepository.findAll();

        //criterios de aceptación
        assertNotNull(categorias);
        assertEquals(2, categorias.size());
    }

    //TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la categoria cuando existe")
    void findById_debeRetornarCategoria_cuandoExiste(){
        Optional<Categoria> resultado = categoriaRepository.findById(electronica.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Electronica", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<Categoria> resultado = categoriaRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    //TEST para findAllNombres() -- Query personalizada del repositorio
    @Test
    @DisplayName("findAllNombres() debe retornar las categorias que coincidan con el nombre exacto")
    void findAllNombres_debeRetornarCategoria_cuandoNombreCoincide(){
        List<Categoria> resultado = categoriaRepository.findAllNombres("Hogar");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hogar", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findAllNombres() debe retornar lista vacia cuando no hay coincidencias")
    void findAllNombres_debeRetornarVacio_cuandoNoHayCoincidencias(){
        List<Categoria> resultado = categoriaRepository.findAllNombres("Inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

}
