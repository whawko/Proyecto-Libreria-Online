package cl.syst3m64.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un usuario")
public class UsuarioRequestDTO {

    @Schema(description = "RUT del usuario", example = "12345678-9")
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15")
    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@correo.cl")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(description = "Clave o contraseña del usuario", example = "miClave123")
    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @Schema(description = "Identificador del rol asignado al usuario", example = "1")
    @NotNull(message = "El rol es obligatorio")
    private Long idRol;

    @Schema(description = "Identificador del estado del usuario", example = "1")
    private Long idEstado;
}

