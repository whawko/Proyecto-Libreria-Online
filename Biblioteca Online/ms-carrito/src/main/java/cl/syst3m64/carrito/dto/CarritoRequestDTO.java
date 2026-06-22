package cl.syst3m64.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para solicitar agregar o actualizar items en el carrito de compras")
public class CarritoRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del usuario propietario del carrito", example = "1")
    private Long idUsuario;

    @NotNull(message = "El ID de libro es obligatorio")
    @Schema(description = "ID del libro a agregar al carrito", example = "10")
    private Long idLibro;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    @Schema(description = "Cantidad de unidades del libro a comprar", example = "2")
    private Integer cantidad;
}
