package cl.syst3m64.resena.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

// ── REQUEST: lo que llega desde Postman ──────────────────────────
@Data
@Schema(description = "DTO de solicitud para crear/actualizar una reseña")
public class ResenaRequestDTO {

    @Schema(description = "Identificador del libro asociado a la reseña", example = "1")
    @NotNull(message = "El id del libro es obligatorio")
    private Long idLibro;

    @Schema(description = "Identificador del usuario que realiza la reseña", example = "1")
    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @Schema(description = "Título de la reseña", example = "Excelente lectura")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String titulo;

    @Schema(description = "Comentario detallado de la reseña", example = "Un libro fascinante con una narrativa envolvente y personajes memorables.")
    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 10, max = 1000, message = "El comentario debe tener entre 10 y 1000 caracteres")
    private String comentario;

    @Schema(description = "Calificación del libro (1 a 5)", example = "5")
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;
}

