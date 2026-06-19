package cl.syst3m64.categorias.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.service.CategoriaService;

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
    public List<Categoria> obtenerTodas(){
        return categoriaService.obtenerTodas();
    }
    @GetMapping("/nombres")
    public List<Categoria> obtenerNombresCat(@RequestParam String nombre){
        return categoriaService.obtenerNombres(nombre);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerId(@PathVariable Long id){
        return categoriaService.obtenerPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria cat){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoriaService.guardar(cat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria cat){
        return categoriaService.actualizar(id, cat)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        if(categoriaService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria no encontrada con ID: " + id);
        }
        categoriaService.eliminar(id);
        return ResponseEntity.ok("Categoria eliminada exitosamente");
    }
}
