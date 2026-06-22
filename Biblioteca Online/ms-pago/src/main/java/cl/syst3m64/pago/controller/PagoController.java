package cl.syst3m64.pago.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.syst3m64.pago.dto.PagoRequestDTO;
import cl.syst3m64.pago.dto.PagoResponseDTO;
import cl.syst3m64.pago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Controlador para gestionar los pagos de las ventas")
public class PagoController {

    private final PagoService pagoService;

    @Operation(summary = "Registrar un pago", description = "Registra un nuevo pago asociado a una venta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(@Valid @RequestBody PagoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrarPago(request));
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna un pago según su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(
            @Parameter(description = "ID del pago", required = true, example = "1") @PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pago por ID de venta", description = "Retorna el pago asociado a una venta específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró pago para la venta indicada")
    })
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<PagoResponseDTO> obtenerPorVenta(
            @Parameter(description = "ID de la venta", required = true, example = "1") @PathVariable Long idVenta) {
        return pagoService.obtenerPagoPorVenta(idVenta)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
