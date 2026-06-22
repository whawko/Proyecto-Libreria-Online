package cl.syst3m64.libros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un libro")
public class LibroResponseDTO {
    @Schema(description = "ID único del libro", example = "1")
    private Long id;
    @Schema(description = "Título del libro", example = "Don Quijote de la Mancha")
    private String titulo;
    @Schema(description = "Descripción del libro", example = "Novela clásica de la literatura española")
    private String descripcion;
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes")
    private String autor;
    @Schema(description = "Código ISBN del libro", example = "978-84-376-0494-7")
    private String isbn;
    @Schema(description = "Precio del libro", example = "15990")
    private float precio;
    @Schema(description = "Año de publicación", example = "1605")
    private String annio;
    @Schema(description = "Editorial del libro", example = "Editorial Planeta")
    private String editorial;
    @Schema(description = "ID de la categoría del libro", example = "1")
    private Long idCategoria;
    @Schema(description = "ID del estado del libro", example = "1")
    private Long idEstado;

}
