package cl.syst3m64.venta.controller;

import java.math.BigDecimal;
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



import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.service.GlobalService;
import tools.jackson.databind.ObjectMapper;

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

    // GET --> api/ventas
    @Test
    @DisplayName("GET api/ventas debe retornar un JSON con la lista de ventas y el codigo 200")
    void traerVentas_debeRetornar200ConListaDeVentas() throws Exception {
        // simular venta
        Venta venta = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        // decirle al servicio que hacer cuando se llame a la funcion que necesito
        when(globalService.traerTodasLasVentas()).thenReturn(List.of(venta));

        // como debe ejecutar la peticion y traer la respuesta para verificarla
        mockMvc.perform(get("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fecha").value("2024-01-15"))
                .andExpect(jsonPath("$[0].total").value(50000));
    }

    // GET --> api/ventas/{id}
    @Test
    @DisplayName("GET api/ventas/{id} debe retornar 200 con la venta cuando existe")
    void traerVentasId_debeRetornar200_cuandoExiste() throws Exception {
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(globalService.traerVentasPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/ventas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fecha").value("2024-01-15"));
    }

    // GET --> api/ventas/{id} cuando no existe
    @Test
    @DisplayName("GET api/ventas/{id} debe retornar 404 cuando la venta no existe")
    void traerVentasId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(globalService.traerVentasPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ventas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // POST --> api/ventas
    @Test
    @DisplayName("POST api/ventas debe retornar 201 con datos validos")
    void crearVenta_debeRetornar201_cuandoDatosValidos() throws Exception {
        VentaRequestDTO request = new VentaRequestDTO("2024-01-15", new BigDecimal("50000"), 1L, 2L);
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(globalService.crearVenta(any(VentaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fecha").value("2024-01-15"));
    }

    // PUT --> api/ventas/{id}
    @Test
    @DisplayName("PUT api/ventas/{id} debe retornar 200 con la venta actualizada")
    void actualizarVenta_debeRetornar200_cuandoExiste() throws Exception {
        VentaRequestDTO request = new VentaRequestDTO("2024-02-20", new BigDecimal("75000"), 1L, 2L);
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-02-20", new BigDecimal("75000"), 1L, 2L);
        when(globalService.actualizarVenta(eq(1L), any(VentaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fecha").value("2024-02-20"))
                .andExpect(jsonPath("$.total").value(75000));
    }

    // DELETE --> api/ventas/{id}
    @Test
    @DisplayName("DELETE api/ventas/{id} debe retornar 200 cuando la venta existe")
    void eliminarVenta_debeRetornar200_cuandoExiste() throws Exception {
        Venta venta = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(globalService.buscarVentaPorId(1L)).thenReturn(Optional.of(venta));

        mockMvc.perform(delete("/api/ventas/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // DELETE --> api/ventas/{id} cuando no existe
    @Test
    @DisplayName("DELETE api/ventas/{id} debe retornar 404 cuando la venta no existe")
    void eliminarVenta_debeRetornar404_cuandoNoExiste() throws Exception {
        when(globalService.buscarVentaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/ventas/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
