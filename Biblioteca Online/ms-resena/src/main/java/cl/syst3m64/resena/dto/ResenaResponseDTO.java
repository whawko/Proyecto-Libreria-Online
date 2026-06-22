package cl.syst3m64.resena.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

// ── RESPONSE: lo que devuelve la API ─────────────────────────────
@Data
@Schema(description = "DTO de respuesta que representa los datos de una reseña")
public class ResenaResponseDTO {

    @Schema(description = "Identificador único de la reseña", example = "1")
    private Long idResena;

    @Schema(description = "Título de la reseña", example = "Excelente lectura")
    private String titulo;

    @Schema(description = "Comentario detallado de la reseña", example = "Un libro fascinante con una narrativa envolvente y personajes memorables.")
    private String comentario;

    @Schema(description = "Calificación del libro (1 a 5)", example = "5")
    private Integer calificacion;

    @Schema(description = "Fecha en que se realizó la reseña", example = "2026-06-21")
    private LocalDate fechaResena;

    @Schema(description = "Estado de la reseña", example = "ACTIVA")
    private String estado;

    // Datos enriquecidos via Feign
    @Schema(description = "Datos del libro asociado a la reseña")
    private LibroDTO libro;

    @Schema(description = "Datos del usuario que realizó la reseña")
    private UsuarioDTO usuario;
}

