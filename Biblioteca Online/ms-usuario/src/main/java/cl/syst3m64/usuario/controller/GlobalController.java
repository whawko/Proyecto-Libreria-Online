package cl.syst3m64.usuario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.syst3m64.usuario.dto.UsuarioRequestDTO;
import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.dto.RolRequestDTO;
import cl.syst3m64.usuario.dto.RolResponseDTO;
import cl.syst3m64.usuario.service.UsuarioService;
import cl.syst3m64.usuario.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios y Roles", description = "Controlador para gestionar los usuarios del sistema y sus roles")
public class GlobalController {
    private final UsuarioService usuarioService;
    private final RolService rolService;

    @Operation(summary = "Listar todos los usuarios", description = "Obtiene la lista completa de usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos(){
        if(usuarioService.obtenerTodosLosUsuarios().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca y retorna un usuario específico según su identificador")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@Valid @PathVariable Long id){
        return usuarioService.obtenerUsuarioPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping()
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
    }

    @Operation(summary = "Actualizar un usuario", description = "Modifica los datos de un usuario existente identificado por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO usuario){
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema según su identificador")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        if(usuarioService.obtenerUsuarioPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ID: " + id);
        }
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    /////////////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Listar todos los roles", description = "Obtiene la lista completa de roles disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente")
    @GetMapping("/roles")
    public ResponseEntity<List<RolResponseDTO>> obtenerRoles(){
        if(rolService.obtenerTodosLosRoles().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(rolService.obtenerTodosLosRoles());
    }

    @Operation(summary = "Obtener rol por ID", description = "Busca y retorna un rol específico según su identificador")
    @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content)
    @GetMapping("/roles/{id}")
    public ResponseEntity<RolResponseDTO> obtenerRolPorId(@PathVariable Long id){
        return rolService.obtenerRolPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Crear un nuevo rol", description = "Registra un nuevo rol en el sistema con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Rol creado exitosamente")
    @PostMapping("/roles")
    public ResponseEntity<RolResponseDTO> crearRol(@Valid @RequestBody RolRequestDTO rol){
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crearRol(rol));
    }

    @Operation(summary = "Actualizar un rol", description = "Modifica los datos de un rol existente identificado por su ID")
    @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content)
    @PutMapping("/roles/{id}")
    public ResponseEntity<RolResponseDTO> actualizarRol(@PathVariable Long id, @Valid @RequestBody RolRequestDTO rol){
        return ResponseEntity.ok(rolService.actualizarRol(id, rol));
    }

    @Operation(summary = "Eliminar un rol", description = "Elimina un rol del sistema según su identificador")
    @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content)
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id){
        if(rolService.obtenerRolPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado con ID: " + id);
        }
        rolService.eliminarRol(id);
        return ResponseEntity.ok("Rol eliminado exitosamente");
    }
}

