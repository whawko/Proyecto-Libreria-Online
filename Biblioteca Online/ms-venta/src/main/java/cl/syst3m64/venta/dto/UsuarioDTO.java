package cl.syst3m64.venta.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String rut;
    private String nombres;
    private String apellidos;
    private String correo;
}
