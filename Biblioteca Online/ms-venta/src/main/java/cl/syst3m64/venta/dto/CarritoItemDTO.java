package cl.syst3m64.venta.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de transporte para items del carrito durante el checkout")
public class CarritoItemDTO {
    @Schema(description = "Identificador único del item en el carrito", example = "1")
    private Long id;

    @Schema(description = "Identificador del usuario propietario del carrito", example = "10")
    private Long idUsuario;

    @Schema(description = "Identificador del libro agregado al carrito", example = "5")
    private Long idLibro;

    @Schema(description = "Cantidad de unidades del libro en el carrito", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario del libro", example = "9990.00")
    private BigDecimal precioUnitario;

    @Schema(description = "Subtotal calculado (cantidad × precio unitario)", example = "19980.00")
    private BigDecimal subtotal;
}
