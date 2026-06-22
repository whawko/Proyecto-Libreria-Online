package cl.syst3m64.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un tipo de estado")
public class TipoEstadoRequestDTO {

    @Schema(description = "Nombre del tipo de estado", example = "Venta")
    @NotBlank(message = "El nombre del tipo de estado es obligatorio")
    private String nombre;
}
