package cl.syst3m64.direccion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionRequestDTO {

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 30, message = "La calle no puede superar los 30 caracteres")
    private String calle;

    @NotNull(message = "El número es obligatorio")
    private Integer numero;

    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "La comuna es obligatoria")
    private Long idComuna;

    @NotNull(message = "El estado es obligatorio")
    private Long idEstado;
}
