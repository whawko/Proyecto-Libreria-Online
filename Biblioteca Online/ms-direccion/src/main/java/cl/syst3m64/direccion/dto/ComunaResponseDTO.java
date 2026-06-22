package cl.syst3m64.direccion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una comuna")
public class ComunaResponseDTO {

    @Schema(description = "ID único de la comuna", example = "45")
    private Long id;

    @Schema(description = "Nombre de la comuna", example = "Providencia")
    private String nombre;

    @Schema(description = "Datos detallados de la región a la que pertenece la comuna")
    private RegionResponseDTO region;
}
