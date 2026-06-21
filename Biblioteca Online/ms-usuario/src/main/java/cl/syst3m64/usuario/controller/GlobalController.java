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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class GlobalController {
    private final UsuarioService usuarioService;
    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos(){
        if(usuarioService.obtenerTodosLosUsuarios().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@Valid @PathVariable Long id){
        return usuarioService.obtenerUsuarioPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping()
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO usuario){
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        if(usuarioService.obtenerUsuarioPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ID: " + id);
        }
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    /////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/roles")
    public ResponseEntity<List<RolResponseDTO>> obtenerRoles(){
        if(rolService.obtenerTodosLosRoles().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(rolService.obtenerTodosLosRoles());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<RolResponseDTO> obtenerRolPorId(@PathVariable Long id){
        return rolService.obtenerRolPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/roles")
    public ResponseEntity<RolResponseDTO> crearRol(@Valid @RequestBody RolRequestDTO rol){
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crearRol(rol));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RolResponseDTO> actualizarRol(@PathVariable Long id, @Valid @RequestBody RolRequestDTO rol){
        return ResponseEntity.ok(rolService.actualizarRol(id, rol));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id){
        if(rolService.obtenerRolPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado con ID: " + id);
        }
        rolService.eliminarRol(id);
        return ResponseEntity.ok("Rol eliminado exitosamente");
    }
}

