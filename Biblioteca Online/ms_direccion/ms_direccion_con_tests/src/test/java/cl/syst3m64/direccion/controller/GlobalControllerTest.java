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



import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.service.GlobalService;
import tools.jackson.databind.ObjectMapper;

//carga la capa web -> GlobalController
//no tenemos acceso a MYSQL, ni JPA, ni repository ni services reales
//no levanta un HTTP (simula las peticiones)
@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {
    //crear un mock de mvc para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    //integrar un mock simulado del service
    @MockitoBean
    private GlobalService globalService;

    //convierta los objetos de JAVA e archivos JSON para los endpoints
    private final ObjectMapper objectMapper = new ObjectMapper();

    //TEST UNIT - Region

    //GET --> /api/direcciones/regiones
    @Test
    @DisplayName("GET /api/direcciones/regiones debe retornar un JSON con la lista de regiones y el codigo 200")
    void obtenerRegiones_debeRetornar200ConListaDeRegiones() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        when(globalService.obtenerRegiones()).thenReturn(List.of(region));

        mockMvc.perform(get("/api/direcciones/regiones")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].nombre").value("Region Metropolitana"));
    }

    //GET --> /api/direcciones/regiones/{id}
    @Test
    @DisplayName("GET /api/direcciones/regiones/{id} debe retornar 200 cuando la region existe")
    void obtenerRegionPorId_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        when(globalService.obtenerRegionPorId(1L)).thenReturn(Optional.of(region));

        mockMvc.perform(get("/api/direcciones/regiones/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Region Metropolitana"));
    }

    @Test
    @DisplayName("GET /api/direcciones/regiones/{id} debe retornar 404 cuando la region no existe")
    void obtenerRegionPorId_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerRegionPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/direcciones/regiones/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //POST --> /api/direcciones/regiones
    @Test
    @DisplayName("POST /api/direcciones/regiones debe retornar 201 con datos validos")
    void crearRegion_debeRetornar201_cuandoDatosValidos() throws Exception{
        Region request = new Region(null, "Region de Valparaiso");
        Region response = new Region(1L, "Region de Valparaiso");
        when(globalService.crearRegion(any(Region.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones/regiones").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nombre").value("Region de Valparaiso"));
    }

    //PUT --> /api/direcciones/regiones/{id}
    @Test
    @DisplayName("PUT /api/direcciones/regiones/{id} debe retornar 200 con datos validos")
    void actualizarRegion_debeRetornar200_cuandoDatosValidos() throws Exception{
        Region request = new Region(null, "Region Metropolitana de Santiago");
        Region response = new Region(1L, "Region Metropolitana de Santiago");
        when(globalService.actualizarRegion(eq(1L), any(Region.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/regiones/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Region Metropolitana de Santiago"));
    }

    //DELETE --> /api/direcciones/regiones/{id}
    @Test
    @DisplayName("DELETE /api/direcciones/regiones/{id} debe retornar 200 cuando la region existe")
    void eliminarRegion_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        when(globalService.obtenerRegionPorId(1L)).thenReturn(Optional.of(region));

        mockMvc.perform(delete("/api/direcciones/regiones/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/direcciones/regiones/{id} debe retornar 404 cuando la region no existe")
    void eliminarRegion_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerRegionPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/direcciones/regiones/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //TEST UNIT - Comuna

    //GET --> /api/direcciones/comunas
    @Test
    @DisplayName("GET /api/direcciones/comunas debe retornar un JSON con la lista de comunas y el codigo 200")
    void obtenerComunas_debeRetornar200ConListaDeComunas() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        when(globalService.obtenerComunas()).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/direcciones/comunas")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombre").value("Santiago"));
    }

    //GET --> /api/direcciones/comunas/{id}
    @Test
    @DisplayName("GET /api/direcciones/comunas/{id} debe retornar 200 cuando la comuna existe")
    void obtenerComunaPorId_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        when(globalService.obtenerComunaPorId(1L)).thenReturn(Optional.of(comuna));

        mockMvc.perform(get("/api/direcciones/comunas/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Santiago"));
    }

    @Test
    @DisplayName("GET /api/direcciones/comunas/{id} debe retornar 404 cuando la comuna no existe")
    void obtenerComunaPorId_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerComunaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/direcciones/comunas/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //POST --> /api/direcciones/comunas
    @Test
    @DisplayName("POST /api/direcciones/comunas debe retornar 201 con datos validos")
    void crearComuna_debeRetornar201_cuandoDatosValidos() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna request = new Comuna(null, "Providencia", region);
        Comuna response = new Comuna(2L, "Providencia", region);
        when(globalService.crearComuna(any(Comuna.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones/comunas").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.nombre").value("Providencia"));
    }

    //PUT --> /api/direcciones/comunas/{id}
    @Test
    @DisplayName("PUT /api/direcciones/comunas/{id} debe retornar 200 con datos validos")
    void actualizarComuna_debeRetornar200_cuandoDatosValidos() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna request = new Comuna(null, "Nunoa", region);
        Comuna response = new Comuna(1L, "Nunoa", region);
        when(globalService.actualizarComuna(eq(1L), any(Comuna.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/comunas/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Nunoa"));
    }

    //DELETE --> /api/direcciones/comunas/{id}
    @Test
    @DisplayName("DELETE /api/direcciones/comunas/{id} debe retornar 200 cuando la comuna existe")
    void eliminarComuna_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        when(globalService.obtenerComunaPorId(1L)).thenReturn(Optional.of(comuna));

        mockMvc.perform(delete("/api/direcciones/comunas/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/direcciones/comunas/{id} debe retornar 404 cuando la comuna no existe")
    void eliminarComuna_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerComunaPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/direcciones/comunas/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //TEST UNIT - Direccion

    //GET --> /api/direcciones
    @Test
    @DisplayName("GET /api/direcciones debe retornar un JSON con la lista de direcciones y el codigo 200")
    void obtenerDirecciones_debeRetornar200ConListaDeDirecciones() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        Direccion direccion = new Direccion(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(globalService.obtenerDirecciones()).thenReturn(List.of(direccion));

        mockMvc.perform(get("/api/direcciones")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].calle").value("Av. Libertador"));
    }

    //GET --> /api/direcciones/{id}
    @Test
    @DisplayName("GET /api/direcciones/{id} debe retornar 200 cuando la direccion existe")
    void obtenerDireccionPorId_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        Direccion direccion = new Direccion(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(globalService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));

        mockMvc.perform(get("/api/direcciones/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.calle").value("Av. Libertador"));
    }

    @Test
    @DisplayName("GET /api/direcciones/{id} debe retornar 404 cuando la direccion no existe")
    void obtenerDireccionPorId_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerDireccionPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/direcciones/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    //POST --> /api/direcciones
    @Test
    @DisplayName("POST /api/direcciones debe retornar 201 con datos validos")
    void crearDireccion_debeRetornar201_cuandoDatosValidos() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        Direccion request = new Direccion(null, "Calle Los Alamos", 567, 1L, comuna, 1L);
        Direccion response = new Direccion(2L, "Calle Los Alamos", 567, 1L, comuna, 1L);
        when(globalService.crearDireccion(any(Direccion.class))).thenReturn(response);

        mockMvc.perform(post("/api/direcciones").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.calle").value("Calle Los Alamos"));
    }

    //PUT --> /api/direcciones/{id}
    @Test
    @DisplayName("PUT /api/direcciones/{id} debe retornar 200 con datos validos")
    void actualizarDireccion_debeRetornar200_cuandoDatosValidos() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        Direccion request = new Direccion(null, "Nueva Calle", 999, 1L, comuna, 1L);
        Direccion response = new Direccion(1L, "Nueva Calle", 999, 1L, comuna, 1L);
        when(globalService.actualizarDireccion(eq(1L), any(Direccion.class))).thenReturn(response);

        mockMvc.perform(put("/api/direcciones/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.calle").value("Nueva Calle"));
    }

    //DELETE --> /api/direcciones/{id}
    @Test
    @DisplayName("DELETE /api/direcciones/{id} debe retornar 200 cuando la direccion existe")
    void eliminarDireccion_debeRetornar200_cuandoExiste() throws Exception{
        Region region = new Region(1L, "Region Metropolitana");
        Comuna comuna = new Comuna(1L, "Santiago", region);
        Direccion direccion = new Direccion(1L, "Av. Libertador", 1234, 1L, comuna, 1L);
        when(globalService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));

        mockMvc.perform(delete("/api/direcciones/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/direcciones/{id} debe retornar 404 cuando la direccion no existe")
    void eliminarDireccion_debeRetornar404_cuandoNoExiste() throws Exception{
        when(globalService.obtenerDireccionPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/direcciones/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

}
