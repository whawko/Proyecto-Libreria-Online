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
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponseDTO> obtenerTodas(){
        return categoriaService.obtenerTodas();
    }

    @GetMapping("/nombres")
    public List<CategoriaResponseDTO> obtenerNombresCat(@RequestParam String nombre){
        return categoriaService.obtenerNombres(nombre);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerId(@PathVariable Long id){
        CategoriaResponseDTO response = categoriaService.obtenerPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaRequestDTO cat){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoriaService.guardar(cat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO cat){
        CategoriaResponseDTO response = categoriaService.actualizar(id, cat)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        categoriaService.obtenerPorId(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con ID: " + id));
        categoriaService.eliminar(id);
        return ResponseEntity.ok("Categoria eliminada exitosamente");
    }
}

