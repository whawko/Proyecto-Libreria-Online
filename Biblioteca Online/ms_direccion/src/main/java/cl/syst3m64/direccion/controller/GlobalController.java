package cl.syst3m64.direccion.controller;

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

import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.service.GlobalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class GlobalController {

    private final GlobalService globalService;

    @GetMapping("/regiones")
    public ResponseEntity<List<Region>> obtenerRegiones(){
        return ResponseEntity.ok(globalService.obtenerRegiones());
    }

    @GetMapping("/regiones/{id}")
    public ResponseEntity<Region> obtenerRegionPorId(@PathVariable Long id){
        return globalService.obtenerRegionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/regiones")
    public ResponseEntity<Region> crearRegion(@Valid @RequestBody Region region){
        return ResponseEntity.status(HttpStatus.CREATED).body(globalService.crearRegion(region));
    }

    @PutMapping("/regiones/{id}")
    public ResponseEntity<Region> actualizarRegion(@PathVariable Long id, @Valid @RequestBody Region region){
        return ResponseEntity.ok(globalService.actualizarRegion(id, region));
    }

    @DeleteMapping("/regiones/{id}")
    public ResponseEntity<?> eliminarRegion(@PathVariable Long id){
        if(globalService.obtenerRegionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Region no encontrada con ID: " + id);
        }
        globalService.eliminarRegion(id);
        return ResponseEntity.ok("Region eliminada exitosamente");
    }

    @GetMapping("/comunas")
    public ResponseEntity<List<Comuna>> obtenerComunas(){
        return ResponseEntity.ok(globalService.obtenerComunas());
    }

    @GetMapping("/comunas/{id}")
    public ResponseEntity<Comuna> obtenerComunaPorId(@PathVariable Long id){
        return globalService.obtenerComunaPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/comunas")
    public ResponseEntity<Comuna> crearComuna(@Valid @RequestBody Comuna comuna){
        return ResponseEntity.status(HttpStatus.CREATED).body(globalService.crearComuna(comuna));
    }

    @PutMapping("/comunas/{id}")
    public ResponseEntity<Comuna> actualizarComuna(@PathVariable Long id, @Valid @RequestBody Comuna comuna){
        return ResponseEntity.ok(globalService.actualizarComuna(id, comuna));
    }

    @DeleteMapping("/comunas/{id}")
    public ResponseEntity<?> eliminarComuna(@PathVariable Long id){
        if(globalService.obtenerComunaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comuna no encontrada con ID: " + id);
        }
        globalService.eliminarComuna(id);
        return ResponseEntity.ok("Comuna eliminada exitosamente");
    }

    @GetMapping
    public ResponseEntity<List<Direccion>> obtenerDirecciones(){
        return ResponseEntity.ok(globalService.obtenerDirecciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id){
        return globalService.obtenerDireccionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    public ResponseEntity<Direccion> crearDireccion(@Valid @RequestBody Direccion direccion){
        return ResponseEntity.status(HttpStatus.CREATED).body(globalService.crearDireccion(direccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Long id, @Valid @RequestBody Direccion direccion){
        return ResponseEntity.ok(globalService.actualizarDireccion(id, direccion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long id){
        if(globalService.obtenerDireccionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Direccion no encontrada con ID: " + id);
        }
        globalService.eliminarDireccion(id);
        return ResponseEntity.ok("Direccion eliminada exitosamente");
    }
}
