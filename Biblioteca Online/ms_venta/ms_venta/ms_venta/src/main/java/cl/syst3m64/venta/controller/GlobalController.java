package cl.syst3m64.venta.controller;

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

import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.service.GlobalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/ventas")
@RequiredArgsConstructor
public class GlobalController {
    private final GlobalService globalService;

    @GetMapping
    public List<Venta> traerVentas(){
        return globalService.traerTodasLasVentas();
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaResponseDTO> traerVentasId(@Valid @PathVariable Long idVenta){
        return globalService.traerVentasPorId(idVenta)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(null));
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(globalService.crearVenta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.ok(globalService.actualizarVenta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id){
        if(globalService.buscarVentaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada con ID: " + id);
        }
        globalService.eliminarVenta(id);
        return ResponseEntity.ok("Venta eliminada exitosamente");
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/detalles")
    public List<Detalle> traerDetalles(){
        return globalService.traerTodosLosDetalles();
    }

    @GetMapping("/detalles/{id}")
    public ResponseEntity<Detalle> traerDetallePorId(@PathVariable Long id){
        return globalService.traerDetallePorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/detalles/venta/{idVenta}")
    public ResponseEntity<Detalle> crearDetalle(@Valid @RequestBody Detalle detalle, @PathVariable Long idVenta){
        return ResponseEntity.status(HttpStatus.CREATED).body(globalService.crearDetalle(detalle, idVenta));
    }

    @PutMapping("/detalles/{id}")
    public ResponseEntity<Detalle> actualizarDetalle(@PathVariable Long id, @Valid @RequestBody Detalle detalle){
        return ResponseEntity.ok(globalService.actualizarDetalle(id, detalle));
    }

    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Long id){
        if(globalService.traerDetallePorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle no encontrado con ID: " + id);
        }
        globalService.eliminarDetalle(id);
        return ResponseEntity.ok("Detalle eliminado exitosamente");
    }
}
