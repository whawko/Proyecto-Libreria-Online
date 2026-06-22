package cl.syst3m64.carrito.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa un item en el carrito de compras como respuesta")
public class CarritoResponseDTO {

    @Schema(description = "ID único del item del carrito", example = "5")
    private Long id;

    @Schema(description = "ID del usuario propietario del carrito", example = "1")
    private Long idUsuario;

    @Schema(description = "ID del libro en el carrito", example = "10")
    private Long idLibro;

    @Schema(description = "Cantidad de unidades seleccionadas", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario del libro", example = "15000.00")
    private BigDecimal precioUnitario;

    @Schema(description = "Subtotal calculado para el item (precioUnitario * cantidad)", example = "30000.00")
    private BigDecimal subtotal;
}
