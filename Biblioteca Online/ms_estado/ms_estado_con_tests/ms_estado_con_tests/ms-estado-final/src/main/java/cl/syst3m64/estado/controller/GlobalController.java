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

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.service.GlobalService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class GlobalController {
    private final GlobalService globalService;
    
    @GetMapping
    public ResponseEntity<List<Estado>> obtenerEstados(){
        return ResponseEntity.ok(globalService.obtenerTodosEstados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> obtenerEstadoPorId(@PathVariable Long id){
        return globalService.obtenerEstadoPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estado> crearEstado(@RequestBody Estado estado){
        return ResponseEntity.ok(globalService.guardarEstado(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Long id){
        if(globalService.obtenerEstadoPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estado no encontrado con ID: " + id);
        }
        globalService.eliminarEstado(id);
        return ResponseEntity.ok("Estado eliminado exitosamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Estado estado){
        return ResponseEntity.ok(globalService.actualizarEstado(id, estado));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/tipos")
    public ResponseEntity<?> obtenerTipoEstados(){
        if(globalService.obtenerTipoEstados().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron tipos de estados");
        }
        return ResponseEntity.ok(globalService.obtenerTipoEstados());
    }

    @GetMapping("/tipos/{idTipo}")
    public ResponseEntity<?> obtenerTipoEstadosPorId(@PathVariable Long idTipo){
        if(globalService.obtenerTipoEstadosPorId(idTipo).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de estados Vacio con ID: " + idTipo);
        }
        return globalService.obtenerTipoEstadosPorId(idTipo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tipos")
    public ResponseEntity<TipoEstado> crearTipoEstado(@RequestBody TipoEstado tipoEstado){
        return ResponseEntity.ok(globalService.guardarTipoEstado(tipoEstado));
    }

    @DeleteMapping("/tipos/{idTipo}")
    public ResponseEntity<?> eliminarTipoEstado(@PathVariable Long idTipo){
        if(globalService.obtenerTipoEstadosPorId(idTipo).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de estado no encontrado con ID: " + idTipo);
        }
        globalService.eliminarTipoEstado(idTipo);
        return ResponseEntity.ok("Tipo de estado eliminado exitosamente");
    }

    @PutMapping("/tipos/{idTipo}")
    public ResponseEntity<?> actualizarTipoEstado(@PathVariable Long idTipo, @RequestBody TipoEstado tipoEstado){
        return ResponseEntity.ok(globalService.actualizarTipoEstado(idTipo, tipoEstado));
    }
}
