package cl.syst3m64.venta.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar una venta")
public class VentaRequestDTO {
    @Schema(description = "Fecha de la venta en formato ISO", example = "2025-06-21")
    private String fecha;

    @Schema(description = "Monto total de la venta", example = "29990.00")
    private BigDecimal total;

    @Schema(description = "Identificador del estado de la venta", example = "1")
    private Long idEstado;

    @Schema(description = "Identificador del usuario que realiza la venta", example = "10")
    private Long idUsuario;
}
