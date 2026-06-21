package cl.syst3m64.estado.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
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

import cl.syst3m64.estado.dto.EstadoRequestDTO;
import cl.syst3m64.estado.dto.EstadoResponseDTO;
import cl.syst3m64.estado.dto.TipoEstadoRequestDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;
import cl.syst3m64.estado.service.EstadoService;
import cl.syst3m64.estado.service.TipoEstadoService;

@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoService estadoService;

    @MockitoBean
    private TipoEstadoService tipoEstadoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/estados debe retornar un JSON con la lista de estados y el codigo 200")
    void obtenerEstados_debeRetornar200ConListaDeEstados() throws Exception {
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");
        EstadoResponseDTO estado = new EstadoResponseDTO(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(estadoService.obtenerTodosEstados()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"))
                .andExpect(jsonPath("$[0].descripcion").value("Venta activa"));
    }

    @Test
    @DisplayName("GET /api/estados/{id} debe retornar 200 con el estado cuando existe")
    void obtenerEstadoPorId_debeRetornar200_cuandoExiste() throws Exception {
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");
        EstadoResponseDTO estado = new EstadoResponseDTO(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(estadoService.obtenerEstadoPorId(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(get("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));
    }

    @Test
    @DisplayName("GET /api/estados/{id} debe retornar 404 cuando el estado no existe")
    void obtenerEstadoPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(estadoService.obtenerEstadoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estados/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/estados debe retornar 201 con el estado creado")
    void crearEstado_debeRetornar21_cuandoDatosValidos() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO("CANCELADA", "Venta cancelada", 1L);
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");
        EstadoResponseDTO response = new EstadoResponseDTO(2L, "CANCELADA", "Venta cancelada", tipoVenta);

        when(estadoService.guardarEstado(any(EstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("CANCELADA"));
    }

    @Test
    @DisplayName("DELETE /api/estados/{id} debe retornar 200 cuando el estado existe")
    void eliminarEstado_debeRetornar200_cuandoExiste() throws Exception {
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");
        EstadoResponseDTO estado = new EstadoResponseDTO(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(estadoService.obtenerEstadoPorId(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(delete("/api/estados/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/estados/{id} debe retornar 200 con el estado actualizado")
    void actualizarEstado_debeRetornar200_cuandoExiste() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO("ACTIVO_MODIFICADO", "Descripcion modificada", 1L);
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");
        EstadoResponseDTO response = new EstadoResponseDTO(1L, "ACTIVO_MODIFICADO", "Descripcion modificada", tipoVenta);

        when(estadoService.actualizarEstado(eq(1L), any(EstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ACTIVO_MODIFICADO"));
    }

    @Test
    @DisplayName("GET /api/estados/tipos debe retornar 200 con la lista de tipos de estado")
    void obtenerTipoEstados_debeRetornar200ConLista() throws Exception {
        TipoEstadoResponseDTO tipoVenta = new TipoEstadoResponseDTO(1L, "VENTA");

        when(tipoEstadoService.obtenerTipoEstados()).thenReturn(List.of(tipoVenta));

        mockMvc.perform(get("/api/estados/tipos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("VENTA"));
    }

    @Test
    @DisplayName("POST /api/estados/tipos debe retornar 201 con el tipo de estado creado")
    void crearTipoEstado_debeRetornar201_cuandoDatosValidos() throws Exception {
        TipoEstadoRequestDTO request = new TipoEstadoRequestDTO("ARRIENDO");
        TipoEstadoResponseDTO response = new TipoEstadoResponseDTO(2L, "ARRIENDO");

        when(tipoEstadoService.guardarTipoEstado(any(TipoEstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados/tipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("ARRIENDO"));
    }
}
