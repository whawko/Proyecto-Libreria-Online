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

import cl.syst3m64.venta.dto.DetalleRequestDTO;
import cl.syst3m64.venta.dto.DetalleResponseDTO;
import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.service.VentaService;
import cl.syst3m64.venta.service.DetalleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @MockitoBean
    private DetalleService detalleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET api/ventas debe retornar un JSON con la lista de ventas y el codigo 200")
    void traerVentas_debeRetornar200ConListaDeVentas() throws Exception {
        VentaResponseDTO venta = new VentaResponseDTO(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(ventaService.traerTodasLasVentas()).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fecha").value("2024-01-15"))
                .andExpect(jsonPath("$[0].total").value(50000));
    }

    @Test
    @DisplayName("GET api/ventas/{id} debe retornar 200 con la venta cuando existe")
    void traerVentasId_debeRetornar200_cuandoExiste() throws Exception {
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(ventaService.traerVentasPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/ventas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fecha").value("2024-01-15"));
    }

    @Test
    @DisplayName("GET api/ventas/{id} cuando no existe debe retornar 404")
    void traerVentasId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(ventaService.traerVentasPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ventas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST api/ventas debe retornar 201 con datos validos")
    void crearVenta_debeRetornar201_cuandoDatosValidos() throws Exception {
        VentaRequestDTO request = new VentaRequestDTO("2024-01-15", new BigDecimal("50000"), 1L, 2L);
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(ventaService.crearVenta(any(VentaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fecha").value("2024-01-15"));
    }

    @Test
    @DisplayName("PUT api/ventas/{id} debe retornar 200 con la venta actualizada")
    void actualizarVenta_debeRetornar200_cuandoExiste() throws Exception {
        VentaRequestDTO request = new VentaRequestDTO("2024-02-20", new BigDecimal("75000"), 1L, 2L);
        VentaResponseDTO response = new VentaResponseDTO(1L, "2024-02-20", new BigDecimal("75000"), 1L, 2L);
        when(ventaService.actualizarVenta(eq(1L), any(VentaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/ventas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fecha").value("2024-02-20"))
                .andExpect(jsonPath("$.total").value(75000));
    }

    @Test
    @DisplayName("DELETE api/ventas/{id} debe retornar 200 cuando la venta existe")
    void eliminarVenta_debeRetornar200_cuandoExiste() throws Exception {
        Venta venta = new Venta(1L, "2024-01-15", new BigDecimal("50000"), 1L, 2L);
        when(ventaService.buscarVentaPorId(1L)).thenReturn(Optional.of(venta));

        mockMvc.perform(delete("/api/ventas/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE api/ventas/{id} cuando no existe debe retornar 404")
    void eliminarVenta_debeRetornar404_cuandoNoExiste() throws Exception {
        when(ventaService.buscarVentaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/ventas/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET api/ventas/detalles debe retornar la lista de detalles")
    void traerDetalles_debeRetornarLista() throws Exception {
        DetalleResponseDTO detalle = new DetalleResponseDTO(1L, 2, new BigDecimal("10000"), 1L, 3L);
        when(detalleService.traerTodosLosDetalles()).thenReturn(List.of(detalle));

        mockMvc.perform(get("/api/ventas/detalles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
