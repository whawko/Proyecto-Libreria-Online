package cl.syst3m64.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de transporte para datos del estado en ms-venta")
public class EstadoDTO {
    @Schema(description = "Identificador único del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "Pendiente")
    private String nombre;

    @Schema(description = "Descripción detallada del estado", example = "La venta está pendiente de pago")
    private String descripcion;
}
