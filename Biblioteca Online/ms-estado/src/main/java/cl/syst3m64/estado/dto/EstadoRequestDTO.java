package cl.syst3m64.estado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoRequestDTO {

    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El ID del tipo de estado es obligatorio")
    private Long idTipoEstado;
}
