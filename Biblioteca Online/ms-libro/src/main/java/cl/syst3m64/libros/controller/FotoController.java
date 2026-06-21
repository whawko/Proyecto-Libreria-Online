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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
public class FotoController {
    private final FotoService fotoService;

    @GetMapping
    public ResponseEntity<?> obtenerTodasLasFotos(){
        List<FotoResponseDTO> fotos = fotoService.obtenerFotos();
        if(fotos.isEmpty()){
            return ResponseEntity.ok("No hay fotos aún");
        }
        return ResponseEntity.ok(fotos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> obtenerFotoPorId(@PathVariable Long id){
        return fotoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada"));
    }

    @PostMapping("/libro/{idLibro}")
    public ResponseEntity<FotoResponseDTO> guardarFoto(@Valid @RequestBody FotoRequestDTO foto, @PathVariable Long idLibro){
        return ResponseEntity.ok(fotoService.guardarFoto(foto, idLibro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> actualizarFoto(@PathVariable Long id, @Valid @RequestBody FotoRequestDTO foto){
        return ResponseEntity.ok(fotoService.actualizarFoto(id, foto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFoto(@PathVariable Long id){
        if(fotoService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Foto no encontrada con ID: " + id);
        }
        fotoService.eliminarFoto(id);
        return ResponseEntity.ok("Foto eliminada exitosamente");
    }
}
