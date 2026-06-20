package cl.syst3m64.libros.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.syst3m64.libros.dto.LibroRequestDTO;
import cl.syst3m64.libros.dto.LibroResponseDTO;
import cl.syst3m64.libros.service.LibroService;

// carga la capa web -> LibroController como GlobalExceptionHandler
// no tenemos acceso a MySQL, ni JPA, ni repository ni services reales
// no levanta un HTTP real (simula las peticiones)
@WebMvcTest(LibroController.class)
@DisplayName("Tests del LibroController con MockMvc")
public class LibroControllerTest {

    // crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // integrar un mock simulado del service
    @MockitoBean
    private LibroService libroService;

    // convierte los objetos de JAVA a archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    // TEST UNIT

    // GET --> /api/libros
    @Test
    @DisplayName("GET /api/libros debe retornar un JSON con la lista de libros y el codigo 200")
    void obtenerTodas_debeRetornar200ConListaDeLibros() throws Exception {
        LibroResponseDTO dto = new LibroResponseDTO(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);

        when(libroService.obtenerLibros()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/libros")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"))
                .andExpect(jsonPath("$[0].autor").value("Cervantes"));
    }

    // GET --> /api/libros/{id}
    @Test
    @DisplayName("GET /api/libros/{id} debe retornar 200 con el libro cuando existe")
    void obtenerPorId_debeRetornar200_cuandoExiste() throws Exception {
        LibroResponseDTO dto = new LibroResponseDTO(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);

        when(libroService.obtenerPorId(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/libros/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    // GET --> /api/libros/{id} cuando no existe
    @Test
    @DisplayName("GET /api/libros/{id} debe retornar 404 cuando el libro no existe")
    void obtenerPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(libroService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/libros/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // POST --> /api/libros
    @Test
    @DisplayName("POST /api/libros debe retornar 201 con el libro creado")
    void crearLibro_debeRetornar201_cuandoDatosValidos() throws Exception {
        LibroRequestDTO request = new LibroRequestDTO("El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        LibroResponseDTO response = new LibroResponseDTO(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);

        when(libroService.guardarLibro(any(LibroRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("El Quijote"));
    }

    // PUT --> /api/libros/{id}
    @Test
    @DisplayName("PUT /api/libros/{id} debe retornar 200 con el libro actualizado")
    void actualizarLibro_debeRetornar200_cuandoExiste() throws Exception {
        LibroRequestDTO request = new LibroRequestDTO("El Quijote Edicion 2", "Novela clasica", "Cervantes", "978-1234", 16000.0f, "1605", "Planeta", 1L, 1L);
        LibroResponseDTO response = new LibroResponseDTO(1L, "El Quijote Edicion 2", "Novela clasica", "Cervantes", "978-1234", 16000.0f, "1605", "Planeta", 1L, 1L);

        when(libroService.actualizarLibro(eq(1L), any(LibroRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El Quijote Edicion 2"));
    }

    // DELETE --> /api/libros/{id}
    @Test
    @DisplayName("DELETE /api/libros/{id} debe retornar 200 cuando el libro existe")
    void eliminarLibro_debeRetornar200_cuandoExiste() throws Exception {
        LibroResponseDTO dto = new LibroResponseDTO(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);

        when(libroService.obtenerPorId(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete("/api/libros/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // DELETE --> /api/libros/{id} cuando no existe
    @Test
    @DisplayName("DELETE /api/libros/{id} debe retornar 404 cuando el libro no existe")
    void eliminarLibro_debeRetornar404_cuandoNoExiste() throws Exception {
        when(libroService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/libros/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
