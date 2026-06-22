package cl.syst3m64.estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un estado")
public class EstadoRequestDTO {

    @Schema(description = "Nombre del estado", example = "Pendiente")
    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombre;

    @Schema(description = "Descripción del estado", example = "Estado inicial de una orden")
    private String descripcion;

    @Schema(description = "ID del tipo de estado al que pertenece", example = "1")
    @NotNull(message = "El ID del tipo de estado es obligatorio")
    private Long idTipoEstado;
}
