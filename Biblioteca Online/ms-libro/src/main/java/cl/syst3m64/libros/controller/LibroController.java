package cl.syst3m64.libros.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.syst3m64.libros.dto.LibroRequestDTO;
import cl.syst3m64.libros.dto.LibroResponseDTO;
import cl.syst3m64.libros.service.LibroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Controlador para gestionar el catálogo de libros de la librería")
public class LibroController {
    private final LibroService libroService;

    @Operation(summary = "Obtener todos los libros", description = "Retorna la lista completa de libros del catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
    @GetMapping
    public List<LibroResponseDTO> obtenerTodas(){
        return libroService.obtenerLibros();
    }

    @Operation(summary = "Obtener libro por ID", description = "Busca y retorna un libro específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerPorId(@Parameter(description = "ID del libro") @PathVariable Long id){
        return libroService.obtenerPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro en el catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping()
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroRequestDTO libro){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(libroService.guardarLibro(libro));
    }

    @Operation(summary = "Actualizar un libro", description = "Actualiza los datos de un libro existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@Parameter(description = "ID del libro") @PathVariable Long id, @Valid @RequestBody LibroRequestDTO libro){
        return ResponseEntity.ok(libroService.actualizarLibro(id, libro));
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro del catálogo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@Parameter(description = "ID del libro") @PathVariable Long id){
        if(libroService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado con ID: " + id);
        }
        libroService.eliminarLibro(id);
        return ResponseEntity.ok("Libro eliminado exitosamente");
    }
}
