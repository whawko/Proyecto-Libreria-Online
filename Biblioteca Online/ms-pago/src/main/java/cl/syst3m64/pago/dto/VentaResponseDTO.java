package cl.syst3m64.pago.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de transporte para datos de la venta en ms-pago")
public class VentaResponseDTO {
    @Schema(description = "ID único de la venta", example = "1")
    private Long id;

    @Schema(description = "Monto total de la venta", example = "29990.00")
    private BigDecimal total;

    @Schema(description = "ID del estado de la venta", example = "1")
    private Long idEstado;
}
