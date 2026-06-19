package com.libreria.resena.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

// ── REQUEST: lo que llega desde Postman ──────────────────────────
@Data
public class ResenaRequestDTO {

    @NotNull(message = "El id del libro es obligatorio")
    private Long idLibro;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String titulo;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 10, max = 1000, message = "El comentario debe tener entre 10 y 1000 caracteres")
    private String comentario;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;
}
