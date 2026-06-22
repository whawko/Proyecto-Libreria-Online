package cl.syst3m64.venta.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un detalle de venta")
public class DetalleRequestDTO {

    @Schema(description = "Cantidad de unidades del libro", example = "2")
    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @Schema(description = "Subtotal calculado para este detalle", example = "19990.00")
    @NotNull(message = "El subtotal es obligatorio")
    private BigDecimal subtotal;

    @Schema(description = "Identificador del libro asociado al detalle", example = "5")
    @NotNull(message = "El libro es obligatorio")
    private Long idLibro;
}
