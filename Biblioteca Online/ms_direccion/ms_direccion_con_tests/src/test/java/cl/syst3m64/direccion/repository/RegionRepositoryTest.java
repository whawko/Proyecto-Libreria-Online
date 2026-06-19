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

import cl.syst3m64.direccion.model.Region;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de regiones en memoria")
public class RegionRepositoryTest {
    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TestEntityManager entityManager;

    //Variables para datos insertados en memoria antes de cada test
    private Region metropolitana;
    private Region valparaiso;

    //insertar datos antes de cada test
    @BeforeEach
    void setUp(){
        metropolitana = entityManager.persistAndFlush(
            new Region(null, "Region Metropolitana")
        );
        valparaiso = entityManager.persistAndFlush(
            new Region(null, "Region de Valparaiso")
        );
    }

    //TEST para el findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las regiones insertadas")
    void findAll_debeRetornarTodasLasRegiones(){
        //logica de negocios que debe ejecutar la prueba
        List<Region> regiones = regionRepository.findAll();

        //criterios de aceptación
        assertNotNull(regiones);
        assertEquals(2, regiones.size());
    }

    //TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la region cuando existe")
    void findById_debeRetornarRegion_cuandoExiste(){
        Optional<Region> resultado = regionRepository.findById(metropolitana.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Region Metropolitana", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<Region> resultado = regionRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

}
