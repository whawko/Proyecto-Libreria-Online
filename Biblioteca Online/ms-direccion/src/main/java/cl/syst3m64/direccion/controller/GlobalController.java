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

import cl.syst3m64.direccion.dto.ComunaRequestDTO;
import cl.syst3m64.direccion.dto.ComunaResponseDTO;
import cl.syst3m64.direccion.dto.DireccionRequestDTO;
import cl.syst3m64.direccion.dto.DireccionResponseDTO;
import cl.syst3m64.direccion.dto.RegionRequestDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.service.RegionService;
import cl.syst3m64.direccion.service.ComunaService;
import cl.syst3m64.direccion.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class GlobalController {

    private final RegionService regionService;
    private final ComunaService comunaService;
    private final DireccionService direccionService;

    @GetMapping("/regiones")
    public ResponseEntity<List<RegionResponseDTO>> obtenerRegiones(){
        return ResponseEntity.ok(regionService.obtenerRegiones());
    }

    @GetMapping("/regiones/{id}")
    public ResponseEntity<RegionResponseDTO> obtenerRegionPorId(@PathVariable Long id){
        return regionService.obtenerRegionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/regiones")
    public ResponseEntity<RegionResponseDTO> crearRegion(@Valid @RequestBody RegionRequestDTO region){
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.crearRegion(region));
    }

    @PutMapping("/regiones/{id}")
    public ResponseEntity<RegionResponseDTO> actualizarRegion(@PathVariable Long id, @Valid @RequestBody RegionRequestDTO region){
        return ResponseEntity.ok(regionService.actualizarRegion(id, region));
    }

    @DeleteMapping("/regiones/{id}")
    public ResponseEntity<?> eliminarRegion(@PathVariable Long id){
        if(regionService.obtenerRegionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Region no encontrada con ID: " + id);
        }
        regionService.eliminarRegion(id);
        return ResponseEntity.ok("Region eliminada exitosamente");
    }

    @GetMapping("/comunas")
    public ResponseEntity<List<ComunaResponseDTO>> obtenerComunas(){
        return ResponseEntity.ok(comunaService.obtenerComunas());
    }

    @GetMapping("/comunas/{id}")
    public ResponseEntity<ComunaResponseDTO> obtenerComunaPorId(@PathVariable Long id){
        return comunaService.obtenerComunaPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/comunas")
    public ResponseEntity<ComunaResponseDTO> crearComuna(@Valid @RequestBody ComunaRequestDTO comuna){
        return ResponseEntity.status(HttpStatus.CREATED).body(comunaService.crearComuna(comuna));
    }

    @PutMapping("/comunas/{id}")
    public ResponseEntity<ComunaResponseDTO> actualizarComuna(@PathVariable Long id, @Valid @RequestBody ComunaRequestDTO comuna){
        return ResponseEntity.ok(comunaService.actualizarComuna(id, comuna));
    }

    @DeleteMapping("/comunas/{id}")
    public ResponseEntity<?> eliminarComuna(@PathVariable Long id){
        if(comunaService.obtenerComunaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comuna no encontrada con ID: " + id);
        }
        comunaService.eliminarComuna(id);
        return ResponseEntity.ok("Comuna eliminada exitosamente");
    }

    @GetMapping
    public ResponseEntity<List<DireccionResponseDTO>> obtenerDirecciones(){
        return ResponseEntity.ok(direccionService.obtenerDirecciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> obtenerDireccionPorId(@PathVariable Long id){
        return direccionService.obtenerDireccionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    public ResponseEntity<DireccionResponseDTO> crearDireccion(@Valid @RequestBody DireccionRequestDTO direccion){
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.crearDireccion(direccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> actualizarDireccion(@PathVariable Long id, @Valid @RequestBody DireccionRequestDTO direccion){
        return ResponseEntity.ok(direccionService.actualizarDireccion(id, direccion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long id){
        if(direccionService.obtenerDireccionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Direccion no encontrada con ID: " + id);
        }
        direccionService.eliminarDireccion(id);
        return ResponseEntity.ok("Direccion eliminada exitosamente");
    }
}
