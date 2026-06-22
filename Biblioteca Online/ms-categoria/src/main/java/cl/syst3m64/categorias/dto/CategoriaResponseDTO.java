package cl.syst3m64.categorias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de una categoría")
public class CategoriaResponseDTO {

    @Schema(description = "ID único de la categoría", example = "2")
    private Long id;

    @Schema(description = "Nombre de la categoría", example = "Novela Histórica")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Libros basados en hechos históricos reales con elementos de ficción")
    private String descripcion;
}
