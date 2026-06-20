package cl.syst3m64.direccion.repository;

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

import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Region;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de comunas en memoria")
public class ComunaRepositoryTest {
    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private TestEntityManager entityManager;

    //Variables para datos insertados en memoria antes de cada test
    private Region region;
    private Comuna santiago;
    private Comuna vina;

    //insertar datos antes de cada test
    @BeforeEach
    void setUp(){
        region = entityManager.persistAndFlush(
            new Region(null, "Region Metropolitana")
        );
        santiago = entityManager.persistAndFlush(
            new Comuna(null, "Santiago", region)
        );
        vina = entityManager.persistAndFlush(
            new Comuna(null, "Vina del Mar", region)
        );
    }

    //TEST para el findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las comunas insertadas")
    void findAll_debeRetornarTodasLasComunas(){
        //logica de negocios que debe ejecutar la prueba
        List<Comuna> comunas = comunaRepository.findAll();

        //criterios de aceptación
        assertNotNull(comunas);
        assertEquals(2, comunas.size());
    }

    //TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la comuna cuando existe")
    void findById_debeRetornarComuna_cuandoExiste(){
        Optional<Comuna> resultado = comunaRepository.findById(santiago.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Santiago", resultado.get().getNombre());
        assertEquals(region.getId(), resultado.get().getIdRegion().getId());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<Comuna> resultado = comunaRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

}
