package cl.syst3m64.direccion.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO de transporte para datos del estado en ms-direccion")
public class EstadoDTO {

    @Schema(description = "ID único del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "ACTIVO")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "El registro se encuentra activo y habilitado")
    private String descripcion;
}
