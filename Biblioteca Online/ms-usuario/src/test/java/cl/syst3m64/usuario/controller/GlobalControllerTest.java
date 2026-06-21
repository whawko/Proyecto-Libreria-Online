package cl.syst3m64.usuario.controller;

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

import cl.syst3m64.usuario.dto.UsuarioRequestDTO;
import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.dto.RolRequestDTO;
import cl.syst3m64.usuario.dto.RolResponseDTO;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.service.UsuarioService;
import cl.syst3m64.usuario.service.RolService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(GlobalController.class)
@DisplayName("Tests del GlobalController con MockMvc")
public class GlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private RolService rolService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/usuarios debe retornar un JSON con la lista de usuarios and el codigo 200")
    void obtenerTodos_debeRetornar200ConListaDeUsuarios() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rol, 1L);

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombres").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} debe retornar 200 con el usuario cuando existe")
    void obtenerPorId_debeRetornar200_cuandoExiste() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rol, 1L);

        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} debe retornar 404 cuando el usuario no existe")
    void obtenerPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/usuarios debe retornar 201 con el usuario creado")
    void crearUsuario_debeRetornar201_cuandoDatosValidos() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        UsuarioRequestDTO request = new UsuarioRequestDTO("12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", 1L, 1L);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rol, 1L);

        when(usuarioService.crearUsuario(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} debe retornar 200 con el usuario actualizado")
    void actualizarUsuario_debeRetornar200_cuandoExiste() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        UsuarioRequestDTO request = new UsuarioRequestDTO("12345678-9", "Juan Modificado", "Perez", "01-01-1990", "juan@mail.com", "clave123", 1L, 1L);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678-9", "Juan Modificado", "Perez", "01-01-1990", "juan@mail.com", "clave123", rol, 1L);

        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Juan Modificado"));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} debe retornar 200 cuando el usuario existe")
    void eliminarUsuario_debeRetornar200_cuandoExiste() throws Exception {
        Rol rol = new Rol(1L, "ADMIN");
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rol, 1L);

        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete("/api/usuarios/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/usuarios/roles debe retornar 200 con la lista de roles DTO")
    void obtenerRoles_debeRetornar200ConListaDeRoles() throws Exception {
        RolResponseDTO rol = new RolResponseDTO(1L, "ADMIN");

        when(rolService.obtenerTodosLosRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/api/usuarios/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
    }

    @Test
    @DisplayName("POST /api/usuarios/roles debe retornar 201 con el rol DTO creado")
    void crearRol_debeRetornar201_cuandoDatosValidos() throws Exception {
        RolRequestDTO request = new RolRequestDTO("CLIENTE");
        RolResponseDTO response = new RolResponseDTO(2L, "CLIENTE");

        when(rolService.crearRol(any(RolRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("CLIENTE"));
    }

}
