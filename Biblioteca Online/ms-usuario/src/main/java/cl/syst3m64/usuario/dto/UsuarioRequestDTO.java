package cl.syst3m64.usuario.dto;

import cl.syst3m64.usuario.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fecha_nacimiento;

    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @NotBlank(message = "El rol es obligatorio")
    private Rol id_rol;

    @Size(max = 50)
    private Long idEstado;
}
