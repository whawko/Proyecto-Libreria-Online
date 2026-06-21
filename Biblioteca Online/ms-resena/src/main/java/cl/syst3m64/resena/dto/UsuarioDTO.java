package cl.syst3m64.resena.dto;

import lombok.Data;

// ── DTO que devuelve ms-usuario via Feign ────────────────────────
@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
}

