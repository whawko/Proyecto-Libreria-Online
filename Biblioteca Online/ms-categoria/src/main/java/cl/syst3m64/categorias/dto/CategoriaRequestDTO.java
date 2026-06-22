package cl.syst3m64.categorias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar una categoría de libros")
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Schema(description = "Nombre de la categoría de libros", example = "Novela Histórica")
    private String nombre;

    @Size(max = 300, message = "La descripción debe tener máximo 300 caracteres")
    @Schema(description = "Descripción detallada del tipo de libros en esta categoría", example = "Libros basados en hechos históricos reales con elementos de ficción")
    private String descripcion;
}
