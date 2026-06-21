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
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de direcciones en memoria")
public class DireccionRepositoryTest {
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private TestEntityManager entityManager;

    //Variables para datos insertados en memoria antes de cada test
    private Direccion direccionUno;
    private Direccion direccionDos;

    //insertar datos antes de cada test
    @BeforeEach
    void setUp(){
        Region region = entityManager.persistAndFlush(
            new Region(null, "Region Metropolitana")
        );
        Comuna comuna = entityManager.persistAndFlush(
            new Comuna(null, "Santiago", region)
        );
        direccionUno = entityManager.persistAndFlush(
            new Direccion(null, "Av. Libertador", 1234, 1L, comuna, 1L)
        );
        direccionDos = entityManager.persistAndFlush(
            new Direccion(null, "Calle Los Alamos", 567, 2L, comuna, 1L)
        );
    }

    //TEST para el findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las direcciones insertadas")
    void findAll_debeRetornarTodasLasDirecciones(){
        //logica de negocios que debe ejecutar la prueba
        List<Direccion> direcciones = direccionRepository.findAll();

        //criterios de aceptación
        assertNotNull(direcciones);
        assertEquals(2, direcciones.size());
    }

    //TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la direccion cuando existe")
    void findById_debeRetornarDireccion_cuandoExiste(){
        Optional<Direccion> resultado = direccionRepository.findById(direccionUno.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Av. Libertador", resultado.get().getCalle());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<Direccion> resultado = direccionRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

}
