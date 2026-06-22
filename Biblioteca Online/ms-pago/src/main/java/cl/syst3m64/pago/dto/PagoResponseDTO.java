package cl.syst3m64.pago.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un pago")
public class PagoResponseDTO {
    @Schema(description = "ID único del pago", example = "1")
    private Long id;

    @Schema(description = "ID de la venta asociada", example = "1")
    private Long idVenta;

    @Schema(description = "Monto del pago", example = "29990.00")
    private BigDecimal monto;

    @Schema(description = "Método de pago utilizado", example = "TARJETA_CREDITO")
    private String metodoPago;

    @Schema(description = "Identificador único de la transacción", example = "TXN-ABC123")
    private String transaccionId;

    @Schema(description = "Fecha en que se realizó el pago", example = "2026-06-21")
    private String fechaPago;

    @Schema(description = "ID del estado del pago", example = "1")
    private Long idEstado;
}
