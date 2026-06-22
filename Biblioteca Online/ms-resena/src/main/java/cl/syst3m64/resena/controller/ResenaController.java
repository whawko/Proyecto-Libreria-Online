package cl.syst3m64.resena.controller;

import cl.syst3m64.resena.dto.ResenaRequestDTO;
import cl.syst3m64.resena.dto.ResenaResponseDTO;
import cl.syst3m64.resena.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Controlador para gestionar las reseñas de libros")
public class ResenaController {

    private final ResenaService resenaService;

    // POST /api/resenas
    @Operation(summary = "Crear una reseña", description = "Crea una nueva reseña para un libro")
    @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente")
    @PostMapping
    public ResponseEntity<ResenaResponseDTO> crear(@Valid @RequestBody ResenaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }

    // GET /api/resenas
    @Operation(summary = "Listar todas las reseñas", description = "Obtiene la lista completa de reseñas registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(resenaService.listarTodas());
    }

    // GET /api/resenas/{id}
    @Operation(summary = "Buscar reseña por ID", description = "Obtiene una reseña específica por su identificador")
    @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(resenaService.buscarPorId(id));
    }

    // GET /api/resenas/libro/{idLibro}
    @Operation(summary = "Listar reseñas por libro", description = "Obtiene todas las reseñas asociadas a un libro específico")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<ResenaResponseDTO>> listarPorLibro(@PathVariable Long idLibro) {
        return ResponseEntity.ok(resenaService.listarPorLibro(idLibro));
    }

    // GET /api/resenas/usuario/{idUsuario}
    @Operation(summary = "Listar reseñas por usuario", description = "Obtiene todas las reseñas realizadas por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ResenaResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(resenaService.listarPorUsuario(idUsuario));
    }

    // PUT /api/resenas/{id}
    @Operation(summary = "Actualizar una reseña", description = "Actualiza los datos de una reseña existente")
    @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequestDTO dto) {
        return ResponseEntity.ok(resenaService.actualizar(id, dto));
    }

    // DELETE /api/resenas/{id}
    @Operation(summary = "Eliminar una reseña", description = "Elimina una reseña existente por su identificador")
    @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

