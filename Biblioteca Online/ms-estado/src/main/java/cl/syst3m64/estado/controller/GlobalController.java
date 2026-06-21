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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class GlobalController {

    private final EstadoService estadoService;
    private final TipoEstadoService tipoEstadoService;

    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> obtenerEstados() {
        return ResponseEntity.ok(estadoService.obtenerTodosEstados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerEstadoPorId(@PathVariable Long id) {
        return estadoService.obtenerEstadoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crearEstado(@Valid @RequestBody EstadoRequestDTO estadoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.guardarEstado(estadoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id) {
        if (estadoService.obtenerEstadoPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado con ID: " + id);
        }
        estadoService.eliminarEstado(id);
        return ResponseEntity.ok("Estado eliminado exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequestDTO estadoDto) {
        return ResponseEntity.ok(estadoService.actualizarEstado(id, estadoDto));
    }

    // =======================================================================================================================

    @GetMapping("/tipos")
    public ResponseEntity<?> obtenerTipoEstados() {
        List<TipoEstadoResponseDTO> tipos = tipoEstadoService.obtenerTipoEstados();
        if (tipos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tipos de estados");
        }
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/tipos/{idTipo}")
    public ResponseEntity<TipoEstadoResponseDTO> obtenerTipoEstadosPorId(@PathVariable Long idTipo) {
        return tipoEstadoService.obtenerTipoEstadosPorId(idTipo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tipos")
    public ResponseEntity<TipoEstadoResponseDTO> crearTipoEstado(@Valid @RequestBody TipoEstadoRequestDTO tipoEstadoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoEstadoService.guardarTipoEstado(tipoEstadoDto));
    }

    @DeleteMapping("/tipos/{idTipo}")
    public ResponseEntity<?> eliminarTipoEstado(@PathVariable Long idTipo) {
        if (tipoEstadoService.obtenerTipoEstadosPorId(idTipo).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de estado no encontrado con ID: " + idTipo);
        }
        tipoEstadoService.eliminarTipoEstado(idTipo);
        return ResponseEntity.ok("Tipo de estado eliminado exitosamente");
    }

    @PutMapping("/tipos/{idTipo}")
    public ResponseEntity<TipoEstadoResponseDTO> actualizarTipoEstado(@PathVariable Long idTipo, @Valid @RequestBody TipoEstadoRequestDTO tipoEstadoDto) {
        return ResponseEntity.ok(tipoEstadoService.actualizarTipoEstado(idTipo, tipoEstadoDto));
    }
}
