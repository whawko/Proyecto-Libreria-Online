package cl.syst3m64.direccion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionResponseDTO {
    private Long id;
    private String calle;
    private Integer numero;
    private Long idUsuario;
    private ComunaResponseDTO comuna;
    private Long idEstado;
}
