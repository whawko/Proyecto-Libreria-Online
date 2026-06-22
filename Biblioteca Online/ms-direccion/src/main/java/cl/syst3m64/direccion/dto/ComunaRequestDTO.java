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
@Schema(description = "DTO de solicitud para crear/actualizar una comuna")
public class ComunaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
    @Schema(description = "Nombre de la comuna", example = "Providencia")
    private String nombre;

    @NotNull(message = "La región es obligatoria")
    @Schema(description = "ID de la región a la que pertenece la comuna", example = "13")
    private Long idRegion;
}
