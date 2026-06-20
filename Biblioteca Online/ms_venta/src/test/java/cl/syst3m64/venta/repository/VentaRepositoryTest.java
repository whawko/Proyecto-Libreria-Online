package cl.syst3m64.venta.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import cl.syst3m64.venta.model.Venta;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de ventas en memoria")
public class VentaRepositoryTest {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Venta venta1;
    private Venta venta2;

    // insertar datos antes de cada test
    @BeforeEach
    void setUp() {
        venta1 = entityManager.persistAndFlush(
                new Venta(null, "2024-01-15", new BigDecimal("50000"), 1L, 2L));
        venta2 = entityManager.persistAndFlush(
                new Venta(null, "2024-02-20", new BigDecimal("75000"), 1L, 3L));
    }

    // TEST para findAll() -- Heredado del JPARepository
    @Test
    @DisplayName("findAll() debe retornar todas las ventas insertadas")
    void findAll_debeRetornarTodasLasVentas() {
        List<Venta> ventas = ventaRepository.findAll();

        assertNotNull(ventas);
        assertEquals(2, ventas.size());
    }

    // TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con la venta cuando existe")
    void findById_debeRetornarVenta_cuandoExiste() {
        Optional<Venta> resultado = ventaRepository.findById(venta1.getId());

        assertTrue(resultado.isPresent());
        assertEquals("2024-01-15", resultado.get().getFecha());
        assertEquals(new BigDecimal("50000"), resultado.get().getTotal());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Venta> resultado = ventaRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    // TEST para save()
    @Test
    @DisplayName("save() debe guardar y retornar la venta con ID generado")
    void save_debeGuardarVentaCorrectamente() {
        Venta nuevaVenta = new Venta(null, "2024-03-10", new BigDecimal("30000"), 2L, 1L);

        Venta guardada = ventaRepository.save(nuevaVenta);

        assertNotNull(guardada.getId());
        assertEquals("2024-03-10", guardada.getFecha());
        assertEquals(new BigDecimal("30000"), guardada.getTotal());
    }

    // TEST para deleteById()
    @Test
    @DisplayName("deleteById() debe eliminar la venta correctamente")
    void deleteById_debeEliminarVenta() {
        ventaRepository.deleteById(venta1.getId());

        Optional<Venta> resultado = ventaRepository.findById(venta1.getId());
        assertFalse(resultado.isPresent());
    }
}
