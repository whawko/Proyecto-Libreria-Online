package cl.syst3m64.venta.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa un detalle de venta")
public class DetalleResponseDTO {
    @Schema(description = "Identificador único del detalle de venta", example = "1")
    private Long id;

    @Schema(description = "Cantidad de unidades del libro", example = "2")
    private Integer cantidad;

    @Schema(description = "Subtotal calculado para este detalle", example = "19990.00")
    private BigDecimal subtotal;

    @Schema(description = "Identificador de la venta asociada", example = "1")
    private Long idVenta;

    @Schema(description = "Identificador del libro asociado al detalle", example = "5")
    private Long idLibro;
}
