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

import cl.syst3m64.libros.dto.FotoRequestDTO;
import cl.syst3m64.libros.dto.FotoResponseDTO;
import cl.syst3m64.libros.service.FotoService;

@WebMvcTest(FotoController.class)
@DisplayName("Tests del FotoController con MockMvc")
public class FotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FotoService fotoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // GET --> /api/fotos
    @Test
    @DisplayName("GET /api/fotos debe retornar 200 con la lista de fotos")
    void obtenerTodasLasFotos_debeRetornar200ConLista() throws Exception {
        FotoResponseDTO foto = new FotoResponseDTO(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", 1L);

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
        FotoResponseDTO foto = new FotoResponseDTO(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", 1L);

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
        FotoRequestDTO request = new FotoRequestDTO("http://imagen.com/foto2.jpg", "foto2", "NO");
        FotoResponseDTO response = new FotoResponseDTO(2L, "http://imagen.com/foto2.jpg", "foto2", "NO", 1L);

        when(fotoService.guardarFoto(any(FotoRequestDTO.class), eq(1L))).thenReturn(response);

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
        FotoRequestDTO request = new FotoRequestDTO("http://imagen.com/foto-actualizada.jpg", "foto-nueva", "SI");
        FotoResponseDTO response = new FotoResponseDTO(1L, "http://imagen.com/foto-actualizada.jpg", "foto-nueva", "SI", 1L);

        when(fotoService.actualizarFoto(eq(1L), any(FotoRequestDTO.class))).thenReturn(response);

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
        FotoResponseDTO foto = new FotoResponseDTO(1L, "http://imagen.com/foto1.jpg", "foto1", "SI", 1L);

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
