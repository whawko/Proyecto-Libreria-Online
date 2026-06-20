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

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.service.GlobalService;

// carga la capa web -> GlobalController como GlobalExceptionHandler
// no tenemos acceso a MySQL, ni JPA, ni repository ni services reales
// no levanta un HTTP real (simula las peticiones)
@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {

    // crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // integrar un mock simulado del service
    @MockitoBean
    private GlobalService globalService;

    // convierte los objetos de JAVA a archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    // TEST UNIT

    // GET --> /api/estados
    @Test
    @DisplayName("GET /api/estados debe retornar un JSON con la lista de estados y el codigo 200")
    void obtenerEstados_debeRetornar200ConListaDeEstados() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");
        Estado estado = new Estado(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(globalService.obtenerTodosEstados()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"))
                .andExpect(jsonPath("$[0].descripcion").value("Venta activa"));
    }

    // GET --> /api/estados/{id}
    @Test
    @DisplayName("GET /api/estados/{id} debe retornar 200 con el estado cuando existe")
    void obtenerEstadoPorId_debeRetornar200_cuandoExiste() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");
        Estado estado = new Estado(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(globalService.obtenerEstadoPorId(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(get("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));
    }

    // GET --> /api/estados/{id} cuando no existe
    @Test
    @DisplayName("GET /api/estados/{id} debe retornar 404 cuando el estado no existe")
    void obtenerEstadoPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(globalService.obtenerEstadoPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estados/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // POST --> /api/estados
    @Test
    @DisplayName("POST /api/estados debe retornar 200 con el estado creado")
    void crearEstado_debeRetornar200_cuandoDatosValidos() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");
        Estado request = new Estado(null, "CANCELADA", "Venta cancelada", tipoVenta);
        Estado response = new Estado(2L, "CANCELADA", "Venta cancelada", tipoVenta);

        when(globalService.guardarEstado(any(Estado.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("CANCELADA"));
    }

    // DELETE --> /api/estados/{id}
    @Test
    @DisplayName("DELETE /api/estados/{id} debe retornar 200 cuando el estado existe")
    void eliminarEstado_debeRetornar200_cuandoExiste() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");
        Estado estado = new Estado(1L, "ACTIVO", "Venta activa", tipoVenta);

        when(globalService.obtenerEstadoPorId(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(delete("/api/estados/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // PUT --> /api/estados/{id}
    @Test
    @DisplayName("PUT /api/estados/{id} debe retornar 200 con el estado actualizado")
    void actualizarEstado_debeRetornar200_cuandoExiste() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");
        Estado request = new Estado(null, "ACTIVO_MODIFICADO", "Descripcion modificada", tipoVenta);
        Estado response = new Estado(1L, "ACTIVO_MODIFICADO", "Descripcion modificada", tipoVenta);

        when(globalService.actualizarEstado(eq(1L), any(Estado.class))).thenReturn(response);

        mockMvc.perform(put("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ACTIVO_MODIFICADO"));
    }

    // GET --> /api/estados/tipos
    @Test
    @DisplayName("GET /api/estados/tipos debe retornar 200 con la lista de tipos de estado")
    void obtenerTipoEstados_debeRetornar200ConLista() throws Exception {
        TipoEstado tipoVenta = new TipoEstado(1L, "VENTA");

        when(globalService.obtenerTipoEstados()).thenReturn(List.of(tipoVenta));

        mockMvc.perform(get("/api/estados/tipos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("VENTA"));
    }

    // POST --> /api/estados/tipos
    @Test
    @DisplayName("POST /api/estados/tipos debe retornar 200 con el tipo de estado creado")
    void crearTipoEstado_debeRetornar200_cuandoDatosValidos() throws Exception {
        TipoEstado request = new TipoEstado(null, "ARRIENDO");
        TipoEstado response = new TipoEstado(2L, "ARRIENDO");

        when(globalService.guardarTipoEstado(any(TipoEstado.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados/tipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("ARRIENDO"));
    }
}
