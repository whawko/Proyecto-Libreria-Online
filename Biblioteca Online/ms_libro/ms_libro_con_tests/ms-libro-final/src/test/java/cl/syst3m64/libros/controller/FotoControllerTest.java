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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.syst3m64.libros.model.Foto;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.service.FotoService;

// carga la capa web -> FotoController como GlobalExceptionHandler
// no tenemos acceso a MySQL, ni JPA, ni repository ni services reales
// no levanta un HTTP real (simula las peticiones)
@WebMvcTest(FotoController.class)
@DisplayName("Tests del FotoController con MockMvc")
public class FotoControllerTest {

    // crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // integrar un mock simulado del service
    @MockitoBean
    private FotoService fotoService;

    // convierte los objetos de JAVA a archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    // TEST UNIT

    // GET --> /api/fotos
    @Test
    @DisplayName("GET /api/fotos debe retornar 200 con la lista de fotos")
    void obtenerTodasLasFotos_debeRetornar200ConLista() throws Exception {
        Libro libro = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        Foto foto = new Foto(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", libro);

        when(fotoService.obtenerFotos()).thenReturn(List.of(foto));

        mockMvc.perform(get("/api/fotos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("http://imagen.com/foto1.jpg"))
                .andExpect(jsonPath("$[0].nombre").value("foto1"));
    }

    // GET --> /api/fotos/{id}
    @Test
    @DisplayName("GET /api/fotos/{id} debe retornar 200 con la foto cuando existe")
    void obtenerFotoPorId_debeRetornar200_cuandoExiste() throws Exception {
        Libro libro = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        Foto foto = new Foto(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", libro);

        when(fotoService.obtenerPorId(1L)).thenReturn(Optional.of(foto));

        mockMvc.perform(get("/api/fotos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value("http://imagen.com/foto1.jpg"));
    }

    // POST --> /api/fotos/libro/{idLibro}
    @Test
    @DisplayName("POST /api/fotos/libro/{idLibro} debe retornar 200 con la foto guardada")
    void guardarFoto_debeRetornar200_cuandoDatosValidos() throws Exception {
        Libro libro = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        Foto request = new Foto(null, "http://imagen.com/foto2.jpg", "foto2", "NO", null);
        Foto response = new Foto(2L, "http://imagen.com/foto2.jpg", "foto2", "NO", libro);

        when(fotoService.guardarFoto(any(Foto.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/fotos/libro/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.url").value("http://imagen.com/foto2.jpg"));
    }

    // PUT --> /api/fotos/{id}
    @Test
    @DisplayName("PUT /api/fotos/{id} debe retornar 200 con la foto actualizada")
    void actualizarFoto_debeRetornar200_cuandoExiste() throws Exception {
        Libro libro = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        Foto request = new Foto(null, "http://imagen.com/foto-actualizada.jpg", "foto-nueva", "SI", null);
        Foto response = new Foto(1L, "http://imagen.com/foto-actualizada.jpg", "foto-nueva", "SI", libro);

        when(fotoService.actualizarFoto(eq(1L), any(Foto.class))).thenReturn(response);

        mockMvc.perform(put("/api/fotos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("foto-nueva"));
    }

    // DELETE --> /api/fotos/{id}
    @Test
    @DisplayName("DELETE /api/fotos/{id} debe retornar 200 cuando la foto existe")
    void eliminarFoto_debeRetornar200_cuandoExiste() throws Exception {
        Libro libro = new Libro(1L, "El Quijote", "Novela clasica", "Cervantes", "978-1234", 15000.0f, "1605", "Planeta", 1L, 1L);
        Foto foto = new Foto(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", libro);

        when(fotoService.obtenerPorId(1L)).thenReturn(Optional.of(foto));

        mockMvc.perform(delete("/api/fotos/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // DELETE --> /api/fotos/{id} cuando no existe
    @Test
    @DisplayName("DELETE /api/fotos/{id} debe retornar 404 cuando la foto no existe")
    void eliminarFoto_debeRetornar404_cuandoNoExiste() throws Exception {
        when(fotoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/fotos/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
