package cl.syst3m64.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un tipo de estado")
public class TipoEstadoResponseDTO {
    @Schema(description = "ID único del tipo de estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo de estado", example = "Venta")
    private String nombre;
}
