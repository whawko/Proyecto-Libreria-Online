package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de transporte para datos de la categoría en ms-libro")
public class CategoriaDTO {
    @Schema(description = "ID de la categoría", example = "1")
    private Long id;
    @Schema(description = "Nombre de la categoría", example = "Ficción")
    private String nombre;
    @Schema(description = "Descripción de la categoría", example = "Libros de ficción y narrativa")
    private String descripcion;
}
