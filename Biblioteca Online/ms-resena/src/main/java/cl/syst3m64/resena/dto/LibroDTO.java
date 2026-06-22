package cl.syst3m64.resena.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// ── DTO que devuelve ms-libro via Feign ──────────────────────────
@Data
@Schema(description = "DTO de transporte para datos del libro en ms-resena")
public class LibroDTO {

    @Schema(description = "Identificador único del libro", example = "1")
    private Long idLibro;

    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String titulo;

    @Schema(description = "Autor del libro", example = "Gabriel García Márquez")
    private String autor;

    @Schema(description = "Código ISBN del libro", example = "978-0-06-088328-7")
    private String isbn;

    @Schema(description = "Género literario del libro", example = "Realismo mágico")
    private String genero;
}

