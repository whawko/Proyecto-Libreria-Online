package cl.syst3m64.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de transporte para datos del estado en ms-usuario")
public class EstadoDTO {
    @Schema(description = "Identificador único del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "Activo")
    private String nombre;

    @Schema(description = "Descripción detallada del estado", example = "Usuario activo en el sistema")
    private String descripcion;
}
