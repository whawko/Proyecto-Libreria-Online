package cl.syst3m64.resena.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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



import cl.syst3m64.resena.dto.LibroDTO;
import cl.syst3m64.resena.dto.ResenaRequestDTO;
import cl.syst3m64.resena.dto.ResenaResponseDTO;
import cl.syst3m64.resena.dto.UsuarioDTO;
import cl.syst3m64.resena.service.ResenaService;

import tools.jackson.databind.ObjectMapper;

// carga la capa web -> ResenaController y GlobalExceptionHandler
// no tenemos acceso a MySQL, ni JPA, ni repository ni services reales
// no levanta un HTTP real (simula las peticiones)
@WebMvcTest(ResenaController.class)
@DisplayName("Tests del ResenaController con MockMvc")
public class ResenaControllerTest {

    // crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // integrar un mock simulado del service
    @MockitoBean
    private ResenaService resenaService;

    // convierte los objetos de JAVA a archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ── Datos de apoyo ────────────────────────────────────────────

    private ResenaResponseDTO buildResponse(Long id) {
        LibroDTO libro = new LibroDTO();
        libro.setIdLibro(1L);
        libro.setTitulo("El Quijote");
        libro.setAutor("Cervantes");

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(2L);
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");

        ResenaResponseDTO r = new ResenaResponseDTO();
        r.setIdResena(id);
        r.setTitulo("Gran libro");
        r.setComentario("Un clasico imprescindible");
        r.setCalificacion(5);
        r.setFechaResena(LocalDate.of(2024, 1, 15));
        r.setEstado("ACTIVO");
        r.setLibro(libro);
        r.setUsuario(usuario);
        return r;
    }

    private ResenaRequestDTO buildRequest() {
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdLibro(1L);
        req.setIdUsuario(2L);
        req.setTitulo("Gran libro");
        req.setComentario("Un clasico imprescindible");
        req.setCalificacion(5);
        return req;
    }

    // TEST UNIT

    // POST --> /api/resenas
    @Test
    @DisplayName("POST /api/resenas debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception {
        ResenaRequestDTO request = buildRequest();
        ResenaResponseDTO response = buildResponse(1L);
        when(resenaService.crear(any(ResenaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idResena").value(1))
                .andExpect(jsonPath("$.titulo").value("Gran libro"))
                .andExpect(jsonPath("$.calificacion").value(5));
    }

    // GET --> /api/resenas
    @Test
    @DisplayName("GET /api/resenas debe retornar 200 con la lista de resenas")
    void listarTodas_debeRetornar200ConLista() throws Exception {
        when(resenaService.listarTodas()).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idResena").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Gran libro"));
    }

    // GET --> /api/resenas/{id}
    @Test
    @DisplayName("GET /api/resenas/{id} debe retornar 200 con la resena cuando existe")
    void buscarPorId_debeRetornar200_cuandoExiste() throws Exception {
        when(resenaService.buscarPorId(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/api/resenas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idResena").value(1))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    // GET --> /api/resenas/libro/{idLibro}
    @Test
    @DisplayName("GET /api/resenas/libro/{idLibro} debe retornar 200 con resenas del libro")
    void listarPorLibro_debeRetornar200ConLista() throws Exception {
        when(resenaService.listarPorLibro(1L)).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/resenas/libro/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].libro.idLibro").value(1));
    }

    // GET --> /api/resenas/usuario/{idUsuario}
    @Test
    @DisplayName("GET /api/resenas/usuario/{idUsuario} debe retornar 200 con resenas del usuario")
    void listarPorUsuario_debeRetornar200ConLista() throws Exception {
        when(resenaService.listarPorUsuario(2L)).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/resenas/usuario/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuario.idUsuario").value(2));
    }

    // PUT --> /api/resenas/{id}
    @Test
    @DisplayName("PUT /api/resenas/{id} debe retornar 200 con la resena actualizada")
    void actualizar_debeRetornar200_cuandoExiste() throws Exception {
        ResenaRequestDTO request = buildRequest();
        ResenaResponseDTO response = buildResponse(1L);
        response.setTitulo("Titulo actualizado");
        when(resenaService.actualizar(eq(1L), any(ResenaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/resenas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo actualizado"));
    }

    // DELETE --> /api/resenas/{id}
    @Test
    @DisplayName("DELETE /api/resenas/{id} debe retornar 204 cuando la resena existe")
    void eliminar_debeRetornar204_cuandoExiste() throws Exception {
        doNothing().when(resenaService).eliminar(1L);

        mockMvc.perform(delete("/api/resenas/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

