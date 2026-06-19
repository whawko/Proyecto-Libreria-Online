package com.libreria.resena.dto;

import lombok.Data;
import java.time.LocalDate;

// ── RESPONSE: lo que devuelve la API ─────────────────────────────
@Data
public class ResenaResponseDTO {

    private Long idResena;
    private String titulo;
    private String comentario;
    private Integer calificacion;
    private LocalDate fechaResena;
    private String estado;

    // Datos enriquecidos via Feign
    private LibroDTO libro;
    private UsuarioDTO usuario;
}
