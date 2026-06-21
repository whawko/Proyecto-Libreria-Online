package cl.syst3m64.estado.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEstadoRequestDTO {

    @NotBlank(message = "El nombre del tipo de estado es obligatorio")
    private String nombre;
}
