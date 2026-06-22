package cl.syst3m64.envio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de transporte para datos de la dirección en ms-envio")
public class DireccionResponseDTO {
    @Schema(description = "ID único de la dirección", example = "1")
    private Long id;

    @Schema(description = "Nombre de la calle", example = "Av. Libertador Bernardo O'Higgins")
    private String calle;

    @Schema(description = "Número de la dirección", example = "1234")
    private String numero;

    @Schema(description = "Comuna de la dirección", example = "Santiago Centro")
    private String comuna;

    @Schema(description = "Ciudad de la dirección", example = "Santiago")
    private String ciudad;
}
