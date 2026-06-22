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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas y Detalles", description = "Controlador para gestionar las ventas, detalles de venta y el proceso de checkout")
public class GlobalController {
    private final VentaService ventaService;
    private final DetalleService detalleService;

    @Operation(summary = "Listar todas las ventas", description = "Retorna una lista con todas las ventas registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente")
    @GetMapping
    public List<VentaResponseDTO> traerVentas(){
        return ventaService.traerTodasLasVentas();
    }

    @Operation(summary = "Obtener venta por ID", description = "Busca y retorna una venta específica según su identificador")
    @ApiResponse(responseCode = "200", description = "Venta encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaResponseDTO> traerVentasId(@Valid @PathVariable Long idVenta){
        return ventaService.traerVentasPorId(idVenta)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(null));
    }

    @Operation(summary = "Crear una nueva venta", description = "Registra una nueva venta en el sistema con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Venta creada exitosamente")
    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearVenta(dto));
    }

    @Operation(summary = "Actualizar una venta", description = "Actualiza los datos de una venta existente según su ID")
    @ApiResponse(responseCode = "200", description = "Venta actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaRequestDTO dto){
        return ResponseEntity.ok(ventaService.actualizarVenta(id, dto));
    }

    @Operation(summary = "Eliminar una venta", description = "Elimina una venta del sistema según su ID")
    @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id){
        if(ventaService.buscarVentaPorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada con ID: " + id);
        }
        ventaService.eliminarVenta(id);
        return ResponseEntity.ok("Venta eliminated exitosamente");
    }

    @Operation(summary = "Realizar checkout", description = "Crea una venta a partir de los items del carrito de un usuario específico")
    @ApiResponse(responseCode = "201", description = "Checkout realizado y venta creada exitosamente")
    @PostMapping("/checkout/{idUsuario}")
    public ResponseEntity<VentaResponseDTO> checkout(@PathVariable Long idUsuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearVentaDesdeCarrito(idUsuario));
    }

    @Operation(summary = "Actualizar estado de una venta", description = "Modifica el estado de una venta existente según el ID del nuevo estado")
    @ApiResponse(responseCode = "200", description = "Estado de la venta actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> actualizarEstadoVenta(@PathVariable Long id, @RequestParam Long idEstado) {
        return ResponseEntity.ok(ventaService.actualizarEstadoVenta(id, idEstado));
    }

    ///////////////////////////////////////////////////////////////////////////

    @Operation(summary = "Listar todos los detalles", description = "Retorna una lista con todos los detalles de venta registrados")
    @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida exitosamente")
    @GetMapping("/detalles")
    public List<DetalleResponseDTO> traerDetalles(){
        return detalleService.traerTodosLosDetalles();
    }

    @Operation(summary = "Obtener detalle por ID", description = "Busca y retorna un detalle de venta específico según su identificador")
    @ApiResponse(responseCode = "200", description = "Detalle encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content)
    @GetMapping("/detalles/{id}")
    public ResponseEntity<DetalleResponseDTO> traerDetallePorId(@PathVariable Long id){
        return detalleService.traerDetallePorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Crear un nuevo detalle de venta", description = "Registra un nuevo detalle asociado a una venta existente")
    @ApiResponse(responseCode = "201", description = "Detalle creado exitosamente")
    @PostMapping("/detalles/venta/{idVenta}")
    public ResponseEntity<DetalleResponseDTO> crearDetalle(@Valid @RequestBody DetalleRequestDTO detalle, @PathVariable Long idVenta){
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleService.crearDetalle(detalle, idVenta));
    }

    @Operation(summary = "Actualizar un detalle de venta", description = "Actualiza los datos de un detalle de venta existente según su ID")
    @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content)
    @PutMapping("/detalles/{id}")
    public ResponseEntity<DetalleResponseDTO> actualizarDetalle(@PathVariable Long id, @Valid @RequestBody DetalleRequestDTO detalle){
        return ResponseEntity.ok(detalleService.actualizarDetalle(id, detalle));
    }

    @Operation(summary = "Eliminar un detalle de venta", description = "Elimina un detalle de venta del sistema según su ID")
    @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Detalle no encontrado", content = @Content)
    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Long id){
        if(detalleService.traerDetallePorId(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle no encontrado con ID: " + id);
        }
        detalleService.eliminarDetalle(id);
        return ResponseEntity.ok("Detalle eliminado exitosamente");
    }
}
