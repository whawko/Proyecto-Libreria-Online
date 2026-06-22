package cl.syst3m64.direccion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una región")
public class RegionResponseDTO {

    @Schema(description = "ID único de la región", example = "13")
    private Long id;

    @Schema(description = "Nombre de la región", example = "Metropolitana de Santiago")
    private String nombre;
}
