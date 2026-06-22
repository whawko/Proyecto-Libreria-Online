package cl.syst3m64.venta.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una venta")
public class VentaResponseDTO {
    @Schema(description = "Identificador único de la venta", example = "1")
    private Long id;

    @Schema(description = "Fecha de la venta en formato ISO", example = "2025-06-21")
    private String fecha;

    @Schema(description = "Monto total de la venta", example = "29990.00")
    private BigDecimal total;

    @Schema(description = "Identificador del estado de la venta", example = "1")
    private Long idEstado;

    @Schema(description = "Identificador del usuario que realizó la venta", example = "10")
    private Long idUsuario;
}
