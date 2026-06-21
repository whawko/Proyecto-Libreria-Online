package cl.syst3m64.direccion.controller;

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

import cl.syst3m64.direccion.dto.ComunaRequestDTO;
import cl.syst3m64.direccion.dto.ComunaResponseDTO;
import cl.syst3m64.direccion.dto.DireccionRequestDTO;
import cl.syst3m64.direccion.dto.DireccionResponseDTO;
import cl.syst3m64.direccion.dto.RegionRequestDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.service.RegionService;
import cl.syst3m64.direccion.service.ComunaService;
import cl.syst3m64.direccion.service.DireccionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegionService regionService;

    @MockitoBean
    private ComunaService comunaService;

    @MockitoBean
    private DireccionService direccionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ================= Region Tests =================

    @Test
    @DisplayName("GET /api/direcciones/regiones debe retornar un JSON con la lista de regiones y el codigo 200")
    void obtenerRegiones_debeRetornar200ConListaDeRegiones() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        when(regionService.obtenerRegiones()).thenReturn(List.of(region));

        mockMvc.perform(get("/api/direcciones/regiones")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Region Metropolitana"));
    }

    @Test
    @DisplayName("GET /api/direcciones/regiones/{id} debe retornar 200 cuando la region existe")
    void obtenerRegionPorId_debeRetornar200_cuandoExiste() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        when(regionService.obtenerRegionPorId(1L)).thenReturn(Optional.of(region));

        mockMvc.perform(get("/api/direcciones/regiones/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Region Metropolitana"));
    }

    @Test
    @DisplayName("GET /api/direcciones/regiones/{id} debe retornar 404 cuando la region no existe")
    void obtenerRegionPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(regionService.obtenerRegionPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/direcciones/regiones/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/direcciones/regiones debe retornar 201 con datos validos")
    void crearRegion_debeRetornar201_cuandoDatosValidos() throws Exception {
        RegionRequestDTO request = new RegionRequestDTO("Region de Valparaiso");
        RegionResponseDTO response = new RegionResponseDTO(1L, "Region de Valparaiso");
        when(regionService.crearRegion(any(RegionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones/regiones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Region de Valparaiso"));
    }

    @Test
    @DisplayName("PUT /api/direcciones/regiones/{id} debe retornar 200 cuando se actualiza correctamente")
    void actualizarRegion_debeRetornar200_cuandoExiste() throws Exception {
        RegionRequestDTO request = new RegionRequestDTO("Metropolitana Modificada");
        RegionResponseDTO response = new RegionResponseDTO(1L, "Metropolitana Modificada");
        when(regionService.actualizarRegion(eq(1L), any(RegionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/regiones/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Metropolitana Modificada"));
    }

    @Test
    @DisplayName("DELETE /api/direcciones/regiones/{id} debe retornar 200 cuando la region existe")
    void eliminarRegion_debeRetornar200_cuandoExiste() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        when(regionService.obtenerRegionPorId(1L)).thenReturn(Optional.of(region));

        mockMvc.perform(delete("/api/direcciones/regiones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Region eliminada exitosamente"));
    }

    // ================= Comuna Tests =================

    @Test
    @DisplayName("GET /api/direcciones/comunas debe retornar un JSON con la lista de comunas")
    void obtenerComunas_debeRetornar200ConLista() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        when(comunaService.obtenerComunas()).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/direcciones/comunas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Santiago"));
    }

    @Test
    @DisplayName("GET /api/direcciones/comunas/{id} debe retornar 200 cuando existe")
    void obtenerComunaPorId_debeRetornar200_cuandoExiste() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        when(comunaService.obtenerComunaPorId(1L)).thenReturn(Optional.of(comuna));

        mockMvc.perform(get("/api/direcciones/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Santiago"));
    }

    @Test
    @DisplayName("POST /api/direcciones/comunas debe retornar 201")
    void crearComuna_debeRetornar201() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaRequestDTO request = new ComunaRequestDTO("Santiago", 1L);
        ComunaResponseDTO response = new ComunaResponseDTO(1L, "Santiago", region);
        when(comunaService.crearComuna(any(ComunaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones/comunas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/direcciones/comunas/{id} debe retornar 200")
    void actualizarComuna_debeRetornar200() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaRequestDTO request = new ComunaRequestDTO("Santiago Modificado", 1L);
        ComunaResponseDTO response = new ComunaResponseDTO(1L, "Santiago Modificado", region);
        when(comunaService.actualizarComuna(eq(1L), any(ComunaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/comunas/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Santiago Modificado"));
    }

    @Test
    @DisplayName("DELETE /api/direcciones/comunas/{id} debe retornar 200")
    void eliminarComuna_debeRetornar200() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        when(comunaService.obtenerComunaPorId(1L)).thenReturn(Optional.of(comuna));

        mockMvc.perform(delete("/api/direcciones/comunas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Comuna eliminada exitosamente"));
    }

    // ================= Direccion Tests =================

    @Test
    @DisplayName("GET /api/direcciones debe retornar un JSON con la lista de direcciones")
    void obtenerDirecciones_debeRetornar200ConLista() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        DireccionResponseDTO direccion = new DireccionResponseDTO(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(direccionService.obtenerDirecciones()).thenReturn(List.of(direccion));

        mockMvc.perform(get("/api/direcciones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calle").value("Av. Libertador"));
    }

    @Test
    @DisplayName("GET /api/direcciones/{id} debe retornar 200")
    void obtenerDireccionPorId_debeRetornar200_cuandoExiste() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        DireccionResponseDTO direccion = new DireccionResponseDTO(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(direccionService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));

        mockMvc.perform(get("/api/direcciones/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calle").value("Av. Libertador"));
    }

    @Test
    @DisplayName("POST /api/direcciones debe retornar 201")
    void crearDireccion_debeRetornar201() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        DireccionRequestDTO request = new DireccionRequestDTO("Av. Libertador", 1234, 1L, 1L, 1L);
        DireccionResponseDTO response = new DireccionResponseDTO(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(direccionService.crearDireccion(any(DireccionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/direcciones/{id} debe retornar 200")
    void actualizarDireccion_debeRetornar200() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        DireccionRequestDTO request = new DireccionRequestDTO("Av. Libertador Modificada", 1234, 1L, 1L, 1L);
        DireccionResponseDTO response = new DireccionResponseDTO(1L, "Av. Libertador Modificada", 1234, 1L, comuna, 1L);
        when(direccionService.actualizarDireccion(eq(1L), any(DireccionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calle").value("Av. Libertador Modificada"));
    }

    @Test
    @DisplayName("DELETE /api/direcciones/{id} debe retornar 200")
    void eliminarDireccion_debeRetornar200() throws Exception {
        RegionResponseDTO region = new RegionResponseDTO(1L, "Region Metropolitana");
        ComunaResponseDTO comuna = new ComunaResponseDTO(1L, "Santiago", region);
        DireccionResponseDTO direccion = new DireccionResponseDTO(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(direccionService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));

        mockMvc.perform(delete("/api/direcciones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Direccion eliminada exitosamente"));
    }
}
