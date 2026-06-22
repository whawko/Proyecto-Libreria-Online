package cl.syst3m64.pago.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar un pago")
public class PagoRequestDTO {

    @Schema(description = "ID de la venta asociada al pago", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la venta es obligatorio")
    private Long idVenta;

    @Schema(description = "Monto del pago", example = "29990.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto mínimo de pago debe ser mayor a 0")
    private BigDecimal monto;

    @Schema(description = "Método de pago utilizado", example = "TARJETA_CREDITO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @Schema(description = "Identificador único de la transacción", example = "TXN-ABC123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El ID de transacción es obligatorio")
    private String transaccionId;

    @Schema(description = "Fecha en que se realizó el pago", example = "2026-06-21", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La fecha de pago es obligatoria")
    private String fechaPago;
}
