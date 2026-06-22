package cl.syst3m64.resena.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// ── DTO que devuelve ms-usuario via Feign ────────────────────────
@Data
@Schema(description = "DTO de transporte para datos del usuario en ms-resena")
public class UsuarioDTO {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long idUsuario;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@correo.cl")
    private String correo;
}

