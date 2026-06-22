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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
@Tag(name = "Direcciones", description = "Controlador para gestionar regiones, comunas y direcciones de los usuarios")
public class GlobalController {

    private final RegionService regionService;
    private final ComunaService comunaService;
    private final DireccionService direccionService;

    @GetMapping("/regiones")
    @Operation(summary = "Obtener todas las regiones", description = "Recupera una lista con todas las regiones registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida exitosamente")
    public ResponseEntity<List<RegionResponseDTO>> obtenerRegiones(){
        return ResponseEntity.ok(regionService.obtenerRegiones());
    }

    @GetMapping("/regiones/{id}")
    @Operation(summary = "Obtener región por ID", description = "Recupera los detalles de una región específica por su identificador.")
    @ApiResponse(responseCode = "200", description = "Región encontrada")
    @ApiResponse(responseCode = "404", description = "Región no encontrada", content = @Content)
    public ResponseEntity<RegionResponseDTO> obtenerRegionPorId(@PathVariable Long id){
        return regionService.obtenerRegionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/regiones")
    @Operation(summary = "Crear región", description = "Registra una nueva región en el sistema.")
    @ApiResponse(responseCode = "201", description = "Región creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    public ResponseEntity<RegionResponseDTO> crearRegion(@Valid @RequestBody RegionRequestDTO region){
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.crearRegion(region));
    }

    @PutMapping("/regiones/{id}")
    @Operation(summary = "Actualizar región", description = "Modifica los datos de una región existente.")
    @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Región no encontrada", content = @Content)
    public ResponseEntity<RegionResponseDTO> actualizarRegion(@PathVariable Long id, @Valid @RequestBody RegionRequestDTO region){
        return ResponseEntity.ok(regionService.actualizarRegion(id, region));
    }

    @DeleteMapping("/regiones/{id}")
    @Operation(summary = "Eliminar región", description = "Elimina de forma permanente una región por su ID.")
    @ApiResponse(responseCode = "200", description = "Región eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Región no encontrada", content = @Content)
    public ResponseEntity<?> eliminarRegion(@PathVariable Long id){
        if(regionService.obtenerRegionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Region no encontrada con ID: " + id);
        }
        regionService.eliminarRegion(id);
        return ResponseEntity.ok("Region eliminada exitosamente");
    }

    @GetMapping("/comunas")
    @Operation(summary = "Obtener todas las comunas", description = "Recupera una lista con todas las comunas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida exitosamente")
    public ResponseEntity<List<ComunaResponseDTO>> obtenerComunas(){
        return ResponseEntity.ok(comunaService.obtenerComunas());
    }

    @GetMapping("/comunas/{id}")
    @Operation(summary = "Obtener comuna por ID", description = "Recupera los detalles de una comuna por su identificador.")
    @ApiResponse(responseCode = "200", description = "Comuna encontrada")
    @ApiResponse(responseCode = "404", description = "Comuna no encontrada", content = @Content)
    public ResponseEntity<ComunaResponseDTO> obtenerComunaPorId(@PathVariable Long id){
        return comunaService.obtenerComunaPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/comunas")
    @Operation(summary = "Crear comuna", description = "Registra una nueva comuna vinculada a una región.")
    @ApiResponse(responseCode = "201", description = "Comuna creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    public ResponseEntity<ComunaResponseDTO> crearComuna(@Valid @RequestBody ComunaRequestDTO comuna){
        return ResponseEntity.status(HttpStatus.CREATED).body(comunaService.crearComuna(comuna));
    }

    @PutMapping("/comunas/{id}")
    @Operation(summary = "Actualizar comuna", description = "Modifica los datos de una comuna existente.")
    @ApiResponse(responseCode = "200", description = "Comuna actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Comuna no encontrada", content = @Content)
    public ResponseEntity<ComunaResponseDTO> actualizarComuna(@PathVariable Long id, @Valid @RequestBody ComunaRequestDTO comuna){
        return ResponseEntity.ok(comunaService.actualizarComuna(id, comuna));
    }

    @DeleteMapping("/comunas/{id}")
    @Operation(summary = "Eliminar comuna", description = "Elimina una comuna por su ID.")
    @ApiResponse(responseCode = "200", description = "Comuna eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Comuna no encontrada", content = @Content)
    public ResponseEntity<?> eliminarComuna(@PathVariable Long id){
        if(comunaService.obtenerComunaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comuna no encontrada con ID: " + id);
        }
        comunaService.eliminarComuna(id);
        return ResponseEntity.ok("Comuna eliminada exitosamente");
    }

    @GetMapping
    @Operation(summary = "Obtener todas las direcciones", description = "Recupera la lista de todas las direcciones de domicilio registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de direcciones obtenida exitosamente")
    public ResponseEntity<List<DireccionResponseDTO>> obtenerDirecciones(){
        return ResponseEntity.ok(direccionService.obtenerDirecciones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dirección por ID", description = "Recupera los detalles de una dirección de domicilio específica.")
    @ApiResponse(responseCode = "200", description = "Dirección encontrada")
    @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content)
    public ResponseEntity<DireccionResponseDTO> obtenerDireccionPorId(@PathVariable Long id){
        return direccionService.obtenerDireccionPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear dirección", description = "Registra una nueva dirección para un usuario.")
    @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    public ResponseEntity<DireccionResponseDTO> crearDireccion(@Valid @RequestBody DireccionRequestDTO direccion){
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.crearDireccion(direccion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección", description = "Modifica los datos de una dirección existente.")
    @ApiResponse(responseCode = "200", description = "Dirección actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content)
    public ResponseEntity<DireccionResponseDTO> actualizarDireccion(@PathVariable Long id, @Valid @RequestBody DireccionRequestDTO direccion){
        return ResponseEntity.ok(direccionService.actualizarDireccion(id, direccion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dirección", description = "Elimina una dirección de domicilio por su ID.")
    @ApiResponse(responseCode = "200", description = "Dirección eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Dirección no encontrada", content = @Content)
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long id){
        if(direccionService.obtenerDireccionPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Direccion no encontrada con ID: " + id);
        }
        direccionService.eliminarDireccion(id);
        return ResponseEntity.ok("Direccion eliminada exitosamente");
    }
}
