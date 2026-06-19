package cl.syst3m64.estado.repository;

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

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de estados en memoria")
public class EstadoRepositoryTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private TipoEstadoRepository tipoEstadoRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Estado estadoActivo;
    private Estado estadoCancelado;

    // insertar datos antes de cada test
    @BeforeEach
    void setUp() {
        TipoEstado tipoVenta = entityManager.persistAndFlush(
            new TipoEstado(null, "VENTA")
        );

        estadoActivo = entityManager.persistAndFlush(
            new Estado(null, "ACTIVO", "Venta activa", tipoVenta)
        );

        estadoCancelado = entityManager.persistAndFlush(
            new Estado(null, "CANCELADA", "Venta cancelada", tipoVenta)
        );
    }

    // TEST para findAll() -- Heredado del JpaRepository
    @Test
    @DisplayName("findAll() debe retornar todos los estados insertados")
    void findAll_debeRetornarTodosLosEstados() {
        List<Estado> estados = estadoRepository.findAll();

        assertNotNull(estados);
        assertEquals(2, estados.size());
    }

    // TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con el estado cuando existe")
    void findById_debeRetornarEstado_cuandoExiste() {
        Optional<Estado> resultado = estadoRepository.findById(estadoActivo.getId());

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Estado> resultado = estadoRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    // TEST para save()
    @Test
    @DisplayName("save() debe persistir un nuevo estado y asignarle un ID")
    void save_debeGuardarEstado_yAsignarId() {
        TipoEstado tipoArriendo = entityManager.persistAndFlush(
            new TipoEstado(null, "ARRIENDO")
        );

        Estado nuevoEstado = new Estado(null, "EN_CURSO", "Arriendo en proceso", tipoArriendo);
        Estado guardado = estadoRepository.save(nuevoEstado);

        assertNotNull(guardado.getId());
        assertEquals("EN_CURSO", guardado.getNombre());
    }

    // TEST para deleteById()
    @Test
    @DisplayName("deleteById() debe eliminar el estado correctamente")
    void deleteById_debeEliminarEstado_cuandoExiste() {
        estadoRepository.deleteById(estadoActivo.getId());

        Optional<Estado> resultado = estadoRepository.findById(estadoActivo.getId());

        assertFalse(resultado.isPresent());
    }
}
