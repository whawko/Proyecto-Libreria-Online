package cl.syst3m64.usuario.dto;

import cl.syst3m64.usuario.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String rut;
    private String nombres;
    private String apellidos;
    private String fecha_nacimiento;
    private String correo;
    private String clave;   
    private Rol id_rol;
    private Long idEstado;
}
