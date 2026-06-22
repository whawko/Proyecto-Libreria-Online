package cl.syst3m64.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un estado")
public class EstadoResponseDTO {
    @Schema(description = "ID único del estado", example = "1")
    private Long id;

    @Schema(description = "Nombre del estado", example = "Pendiente")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "Estado inicial de una orden")
    private String descripcion;

    @Schema(description = "Tipo de estado al que pertenece")
    private TipoEstadoResponseDTO tipoEstado;
}
