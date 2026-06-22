package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de transporte para datos del estado en ms-libro")
public class EstadoDTO {
    @Schema(description = "ID del estado", example = "1")
    private Long id;
    @Schema(description = "Nombre del estado", example = "Disponible")
    private String nombre;
    @Schema(description = "Descripción del estado", example = "El libro está disponible para la venta")
    private String descripcion;
}
