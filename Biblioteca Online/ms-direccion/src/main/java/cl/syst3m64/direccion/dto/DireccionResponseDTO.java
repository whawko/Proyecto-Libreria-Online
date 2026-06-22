package cl.syst3m64.direccion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una dirección de domicilio")
public class DireccionResponseDTO {

    @Schema(description = "ID único de la dirección", example = "3")
    private Long id;

    @Schema(description = "Nombre de la calle", example = "Av. Providencia")
    private String calle;

    @Schema(description = "Número o altura de la calle", example = "1240")
    private Integer numero;

    @Schema(description = "ID del usuario propietario de la dirección", example = "1")
    private Long idUsuario;

    @Schema(description = "Datos detallados de la comuna a la que pertenece")
    private ComunaResponseDTO comuna;

    @Schema(description = "ID del estado de la dirección", example = "1")
    private Long idEstado;
}
