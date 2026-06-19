package com.libreria.resena.controller;

import com.libreria.resena.dto.ResenaRequestDTO;
import com.libreria.resena.dto.ResenaResponseDTO;
import com.libreria.resena.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    // POST /api/resenas
    @PostMapping
    public ResponseEntity<ResenaResponseDTO> crear(@Valid @RequestBody ResenaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }

    // GET /api/resenas
    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(resenaService.listarTodas());
    }

    // GET /api/resenas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(resenaService.buscarPorId(id));
    }

    // GET /api/resenas/libro/{idLibro}
    @GetMapping("/libro/{idLibro}")
    public ResponseEntity<List<ResenaResponseDTO>> listarPorLibro(@PathVariable Long idLibro) {
        return ResponseEntity.ok(resenaService.listarPorLibro(idLibro));
    }

    // GET /api/resenas/usuario/{idUsuario}
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ResenaResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(resenaService.listarPorUsuario(idUsuario));
    }

    // PUT /api/resenas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaRequestDTO dto) {
        return ResponseEntity.ok(resenaService.actualizar(id, dto));
    }

    // DELETE /api/resenas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
