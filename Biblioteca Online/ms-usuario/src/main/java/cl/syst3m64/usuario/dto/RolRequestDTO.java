package cl.syst3m64.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un rol")
public class RolRequestDTO {

    @Schema(description = "Nombre del rol", example = "ADMIN")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre del rol no puede superar los 20 caracteres")
    private String nombre;
}
