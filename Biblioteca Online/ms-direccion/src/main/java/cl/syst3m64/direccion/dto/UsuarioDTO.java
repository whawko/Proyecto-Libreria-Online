package cl.syst3m64.direccion.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO de transporte para datos del usuario en ms-direccion")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez González")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String correo;
}
