package cl.syst3m64.libros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoRequestDTO {

    @NotBlank(message = "La URL es obligatoria")
    @Size(max = 255, message = "La URL no puede superar los 255 caracteres")
    private String url;

    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "La portada no puede superar los 100 caracteres")
    private String portada;
}
