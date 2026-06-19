package cl.syst3m64.categorias.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CategoriaService")
public class CategoriaServiceTest {

    //crear una instancia fict de nuestro repositorio
    //crear un mock del repositorio
    //debido a que no tenemos comunicación con dicho elemento a causa del Test
    @Mock
    private CategoriaRepository categoriaRepository;

    //crear una instancia REAL del CategoriaService
    @InjectMocks
    private CategoriaService categoriaService;

    //Variables para datos de pruebas reutilizables entre TEST
    private Categoria categoriaEjemplo;

    //datos iniciales para las Tests
    //antes de que inicien
    @BeforeEach
    void setUp(){
        categoriaEjemplo = new Categoria(1L, "Electronica", "Articulos electronicos en general");
    }

    // TEST UNIT
    @Test
    @DisplayName("obtenerTodas() retorna la lista de todas las categorias")
    void obtenerTodas_debeRetornarListaDeCategorias(){
        //el repositorio simulado que simule el retorno de datos como si se estuviese conectando a MySQL
        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaEjemplo));

        //configurar la logica o llamar a la funcion del servicio que quiero testear
        List<Categoria> resultado = categoriaService.obtenerTodas();

        //Criterios de aceptación
        //verificar que el resultado no esta vacio
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electronica", resultado.get(0).getNombre());

        //verificar que el findAll() del repositorio fue llamado 1 vez
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar lista vacia cuando no hay categorias en MySQL")
    void obtenerTodas_debeRetornarListaVacia_SiNoHayCategorias(){
        //GIVEN
        when(categoriaRepository.findAll()).thenReturn(List.of());

        //WHEN
        List<Categoria> resultado = categoriaService.obtenerTodas();

        //Criterios de aceptación
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerNombres() retorna las categorias que coinciden con el nombre")
    void obtenerNombres_debeRetornarListaDeCategorias(){
        when(categoriaRepository.findAllNombres("Electronica")).thenReturn(List.of(categoriaEjemplo));

        List<Categoria> resultado = categoriaService.obtenerNombres("Electronica");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electronica", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findAllNombres("Electronica");
    }

    @Test
    @DisplayName("obtenerPorId() retorna la categoria cuando existe")
    void obtenerPorId_debeRetornarCategoria_cuandoExiste(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));

        Optional<Categoria> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Electronica", resultado.get().getNombre());
    }

    @Test
    @DisplayName("obtenerPorId() retorna Optional vacio cuando no existe")
    void obtenerPorId_debeRetornarVacio_cuandoNoExiste(){
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("guardar() debe retornar la categoria creada")
    void guardar_debeRetornarCategoriaCreada(){
        Categoria nueva = new Categoria(null, "Ropa", "Vestuario y accesorios");
        Categoria guardada = new Categoria(2L, "Ropa", "Vestuario y accesorios");
        when(categoriaRepository.save(nueva)).thenReturn(guardada);

        Categoria resultado = categoriaService.guardar(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Ropa", resultado.getNombre());

        verify(categoriaRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("actualizar() debe modificar y retornar la categoria cuando existe")
    void actualizar_debeRetornarCategoriaActualizada_cuandoExiste(){
        Categoria cambios = new Categoria(null, "Electronica Hogar", "Nueva descripcion");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));
        when(categoriaRepository.save(categoriaEjemplo)).thenReturn(categoriaEjemplo);

        Optional<Categoria> resultado = categoriaService.actualizar(1L, cambios);

        assertTrue(resultado.isPresent());
        assertEquals("Electronica Hogar", resultado.get().getNombre());
        assertEquals("Nueva descripcion", resultado.get().getDescripcion());

        verify(categoriaRepository, times(1)).save(categoriaEjemplo);
    }

    @Test
    @DisplayName("actualizar() debe retornar Optional vacio cuando la categoria no existe")
    void actualizar_debeRetornarVacio_cuandoNoExiste(){
        Categoria cambios = new Categoria(null, "Electronica Hogar", "Nueva descripcion");
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.actualizar(99L, cambios);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("eliminar() debe invocar deleteById() del repositorio")
    void eliminar_debeInvocarDeleteByIdDelRepositorio(){
        categoriaService.eliminar(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }

}
