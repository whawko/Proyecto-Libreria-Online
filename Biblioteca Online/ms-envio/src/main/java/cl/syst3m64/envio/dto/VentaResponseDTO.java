package cl.syst3m64.envio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de transporte para datos de la venta en ms-envio")
public class VentaResponseDTO {
    @Schema(description = "ID único de la venta", example = "1")
    private Long id;

    @Schema(description = "Total de la venta", example = "29990.00")
    private BigDecimal total;

    @Schema(description = "ID del estado actual de la venta", example = "2")
    private Long idEstado;
}
