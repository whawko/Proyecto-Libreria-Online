package cl.syst3m64.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de transporte para datos del usuario en ms-venta")
public class UsuarioDTO {
    @Schema(description = "Identificador único del usuario", example = "10")
    private Long id;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez López")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@correo.cl")
    private String correo;
}
