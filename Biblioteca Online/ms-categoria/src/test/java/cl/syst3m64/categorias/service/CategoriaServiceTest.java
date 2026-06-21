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

import cl.syst3m64.categorias.dto.CategoriaRequestDTO;
import cl.syst3m64.categorias.dto.CategoriaResponseDTO;
import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.repository.CategoriaRepository;
import cl.syst3m64.categorias.service.impl.CategoriaServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CategoriaService")
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoriaEjemplo;

    @BeforeEach
    void setUp(){
        categoriaEjemplo = new Categoria(1L, "Electronica", "Articulos electronicos en general");
    }

    @Test
    @DisplayName("obtenerTodas() retorna la lista de todas las categorias DTO")
    void obtenerTodas_debeRetornarListaDeCategorias(){
        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaEjemplo));

        List<CategoriaResponseDTO> resultado = categoriaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electronica", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas() debe retornar lista vacia cuando no hay categorias")
    void obtenerTodas_debeRetornarListaVacia_SiNoHayCategorias(){
        when(categoriaRepository.findAll()).thenReturn(List.of());

        List<CategoriaResponseDTO> resultado = categoriaService.obtenerTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerNombres() retorna las categorias DTO que coinciden con el nombre")
    void obtenerNombres_debeRetornarListaDeCategorias(){
        when(categoriaRepository.findAllNombres("Electronica")).thenReturn(List.of(categoriaEjemplo));

        List<CategoriaResponseDTO> resultado = categoriaService.obtenerNombres("Electronica");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electronica", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findAllNombres("Electronica");
    }

    @Test
    @DisplayName("obtenerPorId() retorna la categoria DTO cuando existe")
    void obtenerPorId_debeRetornarCategoria_cuandoExiste(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));

        Optional<CategoriaResponseDTO> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Electronica", resultado.get().getNombre());
    }

    @Test
    @DisplayName("obtenerPorId() retorna Optional vacio cuando no existe")
    void obtenerPorId_debeRetornarVacio_cuandoNoExiste(){
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CategoriaResponseDTO> resultado = categoriaService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("guardar() debe retornar la categoria DTO creada")
    void guardar_debeRetornarCategoriaCreada(){
        CategoriaRequestDTO nueva = new CategoriaRequestDTO("Ropa", "Vestuario y accesorios");
        Categoria entityNueva = new Categoria(null, "Ropa", "Vestuario y accesorios");
        Categoria guardada = new Categoria(2L, "Ropa", "Vestuario y accesorios");
        when(categoriaRepository.save(entityNueva)).thenReturn(guardada);

        CategoriaResponseDTO resultado = categoriaService.guardar(nueva);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Ropa", resultado.getNombre());

        verify(categoriaRepository, times(1)).save(entityNueva);
    }

    @Test
    @DisplayName("actualizar() debe modificar y retornar la categoria DTO cuando existe")
    void actualizar_debeRetornarCategoriaActualizada_cuandoExiste(){
        CategoriaRequestDTO cambios = new CategoriaRequestDTO("Electronica Hogar", "Nueva descripcion");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));
        when(categoriaRepository.save(categoriaEjemplo)).thenReturn(categoriaEjemplo);

        Optional<CategoriaResponseDTO> resultado = categoriaService.actualizar(1L, cambios);

        assertTrue(resultado.isPresent());
        assertEquals("Electronica Hogar", resultado.get().getNombre());
        assertEquals("Nueva descripcion", resultado.get().getDescripcion());

        verify(categoriaRepository, times(1)).save(categoriaEjemplo);
    }

    @Test
    @DisplayName("actualizar() debe retornar Optional vacio cuando la categoria no existe")
    void actualizar_debeRetornarVacio_cuandoNoExiste(){
        CategoriaRequestDTO cambios = new CategoriaRequestDTO("Electronica Hogar", "Nueva descripcion");
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CategoriaResponseDTO> resultado = categoriaService.actualizar(99L, cambios);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("eliminar() debe invocar deleteById() del repositorio")
    void eliminar_debeInvocarDeleteByIdDelRepositorio(){
        categoriaService.eliminar(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }
}

