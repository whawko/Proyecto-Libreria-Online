package cl.syst3m64.categorias.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.service.CategoriaService;
import tools.jackson.databind.ObjectMapper;

//carga la capa web -> CategoriaController como GlobalExcepcion
//no tenemos acceso a MYSQL, ni JPA, ni repository ni services reales
//no levanta un HTTP (simula las peticiones)
@WebMvcTest(CategoriaController.class)
@DisplayName("Tests del CategoriaController con MockMvc")
public class CategoriaControllerTest {
    //crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    //integrar un mock simulado del service
    @MockitoBean
    private CategoriaService categoriaService;

    //convierta los objetos de JAVA e archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    //TEST UNIT

    //GET --> /api/categorias
    @Test
    @DisplayName("GET /api/categorias debe retornar un JSON con la lista de categorias y el codigo 200")
    void obtenerTodas_debeRetornar200ConListaDeCategorias() throws Exception{
        //simular categorias
        Categoria categoria = new Categoria(1L, "Electronica", "Articulos electronicos en general");
        //decirle al servicio que hacer cuando se llame a la funcion que necesito
        when(categoriaService.obtenerTodas()).thenReturn(List.of(categoria));

        //como debe ejecutar la petición y traer la respuesta para verificarla
        mockMvc.perform(get("/api/categorias")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()) //imprime por consola
        .andExpect(status().isOk()) // HTTP 200
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].nombre").value("Electronica"))
        .andExpect(jsonPath("$[0].descripcion").value("Articulos electronicos en general"));
    }

    //GET --> /api/categorias/nombres
    @Test
    @DisplayName("GET /api/categorias/nombres debe retornar 200 con las categorias filtradas por nombre")
    void obtenerNombresCat_debeRetornar200ConCategoriasFiltradas() throws Exception{
        Categoria categoria = new Categoria(1L, "Hogar", "Articulos para el hogar");
        when(categoriaService.obtenerNombres("Hogar")).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/nombres")
        .param("nombre", "Hogar")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombre").value("Hogar"));
    }

    //GET --> /api/categorias/{id}
    @Test
    @DisplayName("GET /api/categorias/{id} debe retornar 200 cuando la categoria existe")
    void obtenerId_debeRetornar200_cuandoExiste() throws Exception{
        Categoria categoria = new Categoria(1L, "Electronica", "Articulos electronicos en general");
        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Electronica"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} debe retornar 404 cuando la categoria no existe")
    void obtenerId_debeRetornar404_cuandoNoExiste() throws Exception{
        when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //POST --> /api/categorias
    @Test
    @DisplayName("POST /api/categorias debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception{
        //objetos simulados para envio y respuesta del endpoint
        Categoria request = new Categoria(null, "Ropa", "Vestuario y accesorios");
        Categoria response = new Categoria(1L, "Ropa", "Vestuario y accesorios");
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(response);

        //Ejecución y criterios de aceptación
        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated()) //HTTP 201
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nombre").value("Ropa"));
    }

    @Test
    @DisplayName("POST /api/categorias debe retornar 400 cuando el nombre esta vacio")
    void crear_debeRetornar400_cuandoNombreVacio() throws Exception{
        Categoria request = new Categoria(null, "", "Sin nombre");

        mockMvc.perform(post("/api/categorias").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
    }

    //PUT --> /api/categorias/{id}
    @Test
    @DisplayName("PUT /api/categorias/{id} debe retornar 200 cuando la categoria existe")
    void actualizar_debeRetornar200_cuandoExiste() throws Exception{
        Categoria request = new Categoria(null, "Electronica Hogar", "Nueva descripcion");
        Categoria response = new Categoria(1L, "Electronica Hogar", "Nueva descripcion");
        when(categoriaService.actualizar(eq(1L), any(Categoria.class))).thenReturn(Optional.of(response));

        mockMvc.perform(put("/api/categorias/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Electronica Hogar"));
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} debe retornar 404 cuando la categoria no existe")
    void actualizar_debeRetornar404_cuandoNoExiste() throws Exception{
        Categoria request = new Categoria(null, "Electronica Hogar", "Nueva descripcion");
        when(categoriaService.actualizar(eq(99L), any(Categoria.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias/{id}", 99L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
    }

    //DELETE --> /api/categorias/{id}
    @Test
    @DisplayName("DELETE /api/categorias/{id} debe retornar 200 cuando la categoria existe")
    void eliminar_debeRetornar200_cuandoExiste() throws Exception{
        Categoria categoria = new Categoria(1L, "Electronica", "Articulos electronicos en general");
        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(delete("/api/categorias/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} debe retornar 404 cuando la categoria no existe")
    void eliminar_debeRetornar404_cuandoNoExiste() throws Exception{
        when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/categorias/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

}
