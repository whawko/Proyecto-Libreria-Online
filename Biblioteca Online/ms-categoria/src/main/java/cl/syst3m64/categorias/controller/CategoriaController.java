package cl.syst3m64.categorias.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.syst3m64.categorias.dto.CategoriaRequestDTO;
import cl.syst3m64.categorias.dto.CategoriaResponseDTO;
import cl.syst3m64.categorias.service.CategoriaService;
import cl.syst3m64.categorias.exception.RecursoNoEncontradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Controlador para gestionar las categorías de libros")
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Recupera una lista con todas las categorías de libros disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    public List<CategoriaResponseDTO> obtenerTodas(){
        return categoriaService.obtenerTodas();
    }

    @GetMapping("/nombres")
    @Operation(summary = "Buscar categorías por nombre", description = "Busca categorías de libros cuyo nombre coincida con el término provisto.")
    @ApiResponse(responseCode = "200", description = "Categorías encontradas exitosamente")
    public List<CategoriaResponseDTO> obtenerNombresCat(@RequestParam String nombre){
        return categoriaService.obtenerNombres(nombre);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Recupera los detalles de una categoría específica utilizando su identificador.")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<CategoriaResponseDTO> obtenerId(@PathVariable Long id){
        CategoriaResponseDTO response = categoriaService.obtenerPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva categoría de libros en el sistema.")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaRequestDTO cat){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoriaService.guardar(cat));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Modifica los datos de una categoría existente utilizando su identificador.")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<CategoriaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO cat){
        CategoriaResponseDTO response = categoriaService.actualizar(id, cat)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina de forma permanente una categoría específica del sistema.")
    @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        categoriaService.obtenerPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        categoriaService.eliminar(id);
        return ResponseEntity.ok("Categoria eliminada exitosamente");
    }
}

