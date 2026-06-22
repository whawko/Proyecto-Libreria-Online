package cl.syst3m64.usuario.dto;

import cl.syst3m64.usuario.model.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un usuario")
public class UsuarioResponseDTO {
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15")
    private String fechaNacimiento;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@correo.cl")
    private String correo;

    @Schema(description = "Clave o contraseña del usuario", example = "miClave123")
    private String clave;   

    @Schema(description = "Rol asignado al usuario")
    private Rol idRol;

    @Schema(description = "Identificador del estado del usuario", example = "1")
    private Long idEstado;
}
