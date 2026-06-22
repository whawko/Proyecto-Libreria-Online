package cl.syst3m64.libros.controller;

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

import cl.syst3m64.libros.dto.FotoRequestDTO;
import cl.syst3m64.libros.dto.FotoResponseDTO;
import cl.syst3m64.libros.service.FotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
@Tag(name = "Fotos de Libros", description = "Controlador para gestionar las fotos asociadas a los libros")
public class FotoController {
    private final FotoService fotoService;

    @Operation(summary = "Obtener todas las fotos", description = "Retorna la lista completa de fotos de libros")
    @ApiResponse(responseCode = "200", description = "Lista de fotos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<?> obtenerTodasLasFotos(){
        List<FotoResponseDTO> fotos = fotoService.obtenerFotos();
        if(fotos.isEmpty()){
            return ResponseEntity.ok("No hay fotos aún");
        }
        return ResponseEntity.ok(fotos);
    }

    @Operation(summary = "Obtener foto por ID", description = "Busca y retorna una foto específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> obtenerFotoPorId(@Parameter(description = "ID de la foto") @PathVariable Long id){
        return fotoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));
    }

    @Operation(summary = "Registrar una foto para un libro", description = "Asocia una nueva foto a un libro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PostMapping("/libro/{idLibro}")
    public ResponseEntity<FotoResponseDTO> guardarFoto(@Valid @RequestBody FotoRequestDTO foto, @Parameter(description = "ID del libro") @PathVariable Long idLibro){
        return ResponseEntity.ok(fotoService.guardarFoto(foto, idLibro));
    }

    @Operation(summary = "Actualizar una foto", description = "Actualiza los datos de una foto existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> actualizarFoto(@Parameter(description = "ID de la foto") @PathVariable Long id, @Valid @RequestBody FotoRequestDTO foto){
        return ResponseEntity.ok(fotoService.actualizarFoto(id, foto));
    }

    @Operation(summary = "Eliminar una foto", description = "Elimina una foto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFoto(@Parameter(description = "ID de la foto") @PathVariable Long id){
        if(fotoService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Foto no encontrada con ID: " + id);
        }
        fotoService.eliminarFoto(id);
        return ResponseEntity.ok("Foto eliminada exitosamente");
    }
}
