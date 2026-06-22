package cl.syst3m64.estado.controller;

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
import cl.syst3m64.estado.dto.EstadoRequestDTO;
import cl.syst3m64.estado.dto.EstadoResponseDTO;
import cl.syst3m64.estado.dto.TipoEstadoRequestDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;
import cl.syst3m64.estado.service.EstadoService;
import cl.syst3m64.estado.service.TipoEstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
@Tag(name = "Estados", description = "Controlador para gestionar estados y tipos de estado del sistema")
public class GlobalController {

    private final EstadoService estadoService;
    private final TipoEstadoService tipoEstadoService;

    @Operation(summary = "Listar todos los estados", description = "Recupera la lista completa de estados del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente", content = @Content)
    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> obtenerEstados() {
        return ResponseEntity.ok(estadoService.obtenerTodosEstados());
    }

    @Operation(summary = "Obtener estado por ID", description = "Recupera los detalles de un estado específico por su ID")
    @ApiResponse(responseCode = "200", description = "Estado encontrado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerEstadoPorId(@PathVariable Long id) {
        return estadoService.obtenerEstadoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear estado", description = "Registra un nuevo estado en el sistema")
    @ApiResponse(responseCode = "201", description = "Estado creado exitosamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de estado inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crearEstado(@Valid @RequestBody EstadoRequestDTO estadoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.guardarEstado(estadoDto));
    }

    @Operation(summary = "Eliminar estado", description = "Elimina un estado del sistema por su ID")
    @ApiResponse(responseCode = "200", description = "Estado eliminado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id) {
        if (estadoService.obtenerEstadoPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado con ID: " + id);
        }
        estadoService.eliminarEstado(id);
        return ResponseEntity.ok("Estado eliminado exitosamente");
    }

    @Operation(summary = "Actualizar estado", description = "Actualiza los datos de un estado existente por su ID")
    @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de estado inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequestDTO estadoDto) {
        return ResponseEntity.ok(estadoService.actualizarEstado(id, estadoDto));
    }

    // =======================================================================================================================

    @Operation(summary = "Listar todos los tipos de estado", description = "Recupera la lista completa de tipos de estado del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de estado obtenida exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "No se encontraron tipos de estado", content = @Content)
    @GetMapping("/tipos")
    public ResponseEntity<?> obtenerTipoEstados() {
        List<TipoEstadoResponseDTO> tipos = tipoEstadoService.obtenerTipoEstados();
        if (tipos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tipos de estados");
        }
        return ResponseEntity.ok(tipos);
    }

    @Operation(summary = "Obtener tipo de estado por ID", description = "Recupera los detalles de un tipo de estado específico por su ID")
    @ApiResponse(responseCode = "200", description = "Tipo de estado encontrado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tipo de estado no encontrado", content = @Content)
    @GetMapping("/tipos/{idTipo}")
    public ResponseEntity<TipoEstadoResponseDTO> obtenerTipoEstadosPorId(@PathVariable Long idTipo) {
        return tipoEstadoService.obtenerTipoEstadosPorId(idTipo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear tipo de estado", description = "Registra un nuevo tipo de estado en el sistema")
    @ApiResponse(responseCode = "201", description = "Tipo de estado creado exitosamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de tipo de estado inválidos", content = @Content)
    @PostMapping("/tipos")
    public ResponseEntity<TipoEstadoResponseDTO> crearTipoEstado(@Valid @RequestBody TipoEstadoRequestDTO tipoEstadoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoEstadoService.guardarTipoEstado(tipoEstadoDto));
    }

    @Operation(summary = "Eliminar tipo de estado", description = "Elimina un tipo de estado del sistema por su ID")
    @ApiResponse(responseCode = "200", description = "Tipo de estado eliminado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tipo de estado no encontrado", content = @Content)
    @DeleteMapping("/tipos/{idTipo}")
    public ResponseEntity<?> eliminarTipoEstado(@PathVariable Long idTipo) {
        if (tipoEstadoService.obtenerTipoEstadosPorId(idTipo).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de estado no encontrado con ID: " + idTipo);
        }
        tipoEstadoService.eliminarTipoEstado(idTipo);
        return ResponseEntity.ok("Tipo de estado eliminado exitosamente");
    }

    @Operation(summary = "Actualizar tipo de estado", description = "Actualiza los datos de un tipo de estado existente por su ID")
    @ApiResponse(responseCode = "200", description = "Tipo de estado actualizado exitosamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de tipo de estado inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tipo de estado no encontrado", content = @Content)
    @PutMapping("/tipos/{idTipo}")
    public ResponseEntity<TipoEstadoResponseDTO> actualizarTipoEstado(@PathVariable Long idTipo, @Valid @RequestBody TipoEstadoRequestDTO tipoEstadoDto) {
        return ResponseEntity.ok(tipoEstadoService.actualizarTipoEstado(idTipo, tipoEstadoDto));
    }
}
