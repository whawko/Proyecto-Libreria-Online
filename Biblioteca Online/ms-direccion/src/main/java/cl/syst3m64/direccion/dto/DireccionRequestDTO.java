package cl.syst3m64.direccion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar o actualizar una dirección de domicilio")
public class DireccionRequestDTO {

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 30, message = "La calle no puede superar los 30 caracteres")
    @Schema(description = "Nombre de la calle", example = "Av. Providencia")
    private String calle;

    @NotNull(message = "El número es obligatorio")
    @Schema(description = "Número o altura de la calle", example = "1240")
    private Integer numero;

    @NotNull(message = "El usuario es obligatorio")
    @Schema(description = "ID del usuario propietario de la dirección", example = "1")
    private Long idUsuario;

    @NotNull(message = "La comuna es obligatoria")
    @Schema(description = "ID de la comuna correspondiente", example = "45")
    private Long idComuna;

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "ID del estado de la dirección", example = "1")
    private Long idEstado;
}
