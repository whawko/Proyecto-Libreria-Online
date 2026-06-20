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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService libroService;

    @GetMapping
    public List<LibroResponseDTO> obtenerTodas(){
        return libroService.obtenerLibros();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerPorId(@PathVariable Long id){
        return libroService.obtenerPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroRequestDTO libro){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(libroService.guardarLibro(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroRequestDTO libro){
        return ResponseEntity.ok(libroService.actualizarLibro(id, libro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Long id){
        if(libroService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado con ID: " + id);
        }
        libroService.eliminarLibro(id);
        return ResponseEntity.ok("Libro eliminado exitosamente");
    }
}
