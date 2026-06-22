package cl.syst3m64.envio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.syst3m64.envio.dto.EnvioRequestDTO;
import cl.syst3m64.envio.dto.EnvioResponseDTO;
import cl.syst3m64.envio.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Controlador para gestionar los envíos y despachos de las ventas")
public class EnvioController {

    private final EnvioService envioService;

    @Operation(summary = "Registrar envío", description = "Registra un nuevo envío para una venta pagada")
    @ApiResponse(responseCode = "201", description = "Envío registrado exitosamente", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de envío inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<EnvioResponseDTO> registrarEnvio(@Valid @RequestBody EnvioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.registrarEnvio(request));
    }

    @Operation(summary = "Obtener envío por ID", description = "Recupera los detalles de un envío específico")
    @ApiResponse(responseCode = "200", description = "Envío encontrado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Envío no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return envioService.obtenerEnvioPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener envío por venta", description = "Recupera el envío asociado a una venta")
    @ApiResponse(responseCode = "200", description = "Envío encontrado exitosamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Envío no encontrado para la venta especificada", content = @Content)
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<EnvioResponseDTO> obtenerPorVenta(@PathVariable Long idVenta) {
        return envioService.obtenerEnvioPorVenta(idVenta)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
