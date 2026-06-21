package cl.syst3m64.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20, message = "El nombre del rol no puede superar los 20 caracteres")
    private String nombre;
}
