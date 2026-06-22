package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud para crear/actualizar un libro")
public class LibroRequestDTO {

    @Schema(description = "Título del libro", example = "Don Quijote de la Mancha")
    @NotBlank(message="El titulo es obligatorio")
    private String titulo;

    @Schema(description = "Descripción del libro", example = "Novela clásica de la literatura española")
    @NotBlank(message="La descripcion es obligatoria")
    private String descripcion;

    @Schema(description = "Autor del libro", example = "Miguel de Cervantes")
    @NotBlank(message="El autor es obligatorio")
    private String autor;

    @Schema(description = "Código ISBN del libro", example = "978-84-376-0494-7")
    @NotBlank(message="El isbn es obligatorio")
    private String isbn;

    @Schema(description = "Precio del libro", example = "15990")
    @Min(value=1,message="El precio debe ser mayor a 1")
    private float precio;

    @Schema(description = "Año de publicación", example = "1605")
    @NotBlank(message="El año es obligatorio")
    private String annio;

    @Schema(description = "Editorial del libro", example = "Editorial Planeta")
    @NotBlank(message="La editorial es obligatoria")
    private String editorial;

    @Schema(description = "ID de la categoría del libro", example = "1")
    @NotNull(message="El id de la categoria es obligatorio")
    private Long idCategoria;

    @Schema(description = "ID del estado del libro", example = "1")
    @NotNull(message="El id de el estado es obligatorio")
    private Long idEstado;


}
