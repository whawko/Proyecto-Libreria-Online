package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar una foto de un libro")
public class FotoRequestDTO {

    @Schema(description = "URL de la imagen", example = "https://ejemplo.com/foto.jpg")
    @NotBlank(message = "La URL es obligatoria")
    @Size(max = 255, message = "La URL no puede superar los 255 caracteres")
    private String url;

    @Schema(description = "Nombre descriptivo de la foto", example = "Portada principal")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Schema(description = "Indicador de portada", example = "S")
    @Size(max = 100, message = "La portada no puede superar los 100 caracteres")
    private String portada;
}
