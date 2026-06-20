package com.libreria.resena.dto;

import lombok.Data;

// ── DTO que devuelve ms-libro via Feign ──────────────────────────
@Data
public class LibroDTO {
    private Long idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private String genero;
}
