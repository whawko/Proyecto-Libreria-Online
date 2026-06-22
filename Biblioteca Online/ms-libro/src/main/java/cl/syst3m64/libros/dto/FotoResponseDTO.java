package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una foto")
public class FotoResponseDTO {
    @Schema(description = "ID único de la foto", example = "1")
    private Long id;
    @Schema(description = "URL de la imagen", example = "https://ejemplo.com/foto.jpg")
    private String url;
    @Schema(description = "Nombre descriptivo de la foto", example = "Portada principal")
    private String nombre;
    @Schema(description = "Indicador de portada", example = "S")
    private String portada;
    @Schema(description = "ID del libro asociado", example = "1")
    private Long idLibro;
}
