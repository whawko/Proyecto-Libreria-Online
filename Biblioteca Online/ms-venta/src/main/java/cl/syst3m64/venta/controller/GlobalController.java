package cl.syst3m64.venta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.syst3m64.venta.dto.DetalleRequestDTO;
import cl.syst3m64.venta.dto.DetalleResponseDTO;
import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.service.VentaService;
import cl.syst3m64.venta.service.DetalleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/ventas")
@RequiredArgsConstructor
public class GlobalController {
    private final VentaService ventaService;
    private final DetalleService detalleService;

    @GetMapping
    public List<VentaResponseDTO> traerVentas(){
        return ventaService.traerTodasLasVentas();
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaResponseDTO> traerVentasId(@Valid @PathVariable Long idVenta){
        return ventaService.traerVentasPorId(idVenta)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(null));
    }

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearVenta(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.ok(ventaService.actualizarVenta(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id){
        if(ventaService.buscarVentaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada con ID: " + id);
        }
        ventaService.eliminarVenta(id);
        return ResponseEntity.ok("Venta eliminated exitosamente");
    }

    @PostMapping("/checkout/{idUsuario}")
    public ResponseEntity<VentaResponseDTO> checkout(@PathVariable Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearVentaDesdeCarrito(idUsuario));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> actualizarEstadoVenta(@PathVariable Long id, @RequestParam Long idEstado) {
        return ResponseEntity.ok(ventaService.actualizarEstadoVenta(id, idEstado));
    }

    ///////////////////////////////////////////////////////////////////////////

    @GetMapping("/detalles")
    public List<DetalleResponseDTO> traerDetalles(){
        return detalleService.traerTodosLosDetalles();
    }

    @GetMapping("/detalles/{id}")
    public ResponseEntity<DetalleResponseDTO> traerDetallePorId(@PathVariable Long id){
        return detalleService.traerDetallePorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/detalles/venta/{idVenta}")
    public ResponseEntity<DetalleResponseDTO> crearDetalle(@Valid @RequestBody DetalleRequestDTO detalle, @PathVariable Long idVenta){
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleService.crearDetalle(detalle, idVenta));
    }

    @PutMapping("/detalles/{id}")
    public ResponseEntity<DetalleResponseDTO> actualizarDetalle(@PathVariable Long id, @Valid @RequestBody DetalleRequestDTO detalle){
        return ResponseEntity.ok(detalleService.actualizarDetalle(id, detalle));
    }

    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Long id){
        if(detalleService.traerDetallePorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle no encontrado con ID: " + id);
        }
        detalleService.eliminarDetalle(id);
        return ResponseEntity.ok("Detalle eliminado exitosamente");
    }
}
