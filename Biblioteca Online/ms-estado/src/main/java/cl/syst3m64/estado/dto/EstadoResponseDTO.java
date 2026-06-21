package cl.syst3m64.estado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoEstadoResponseDTO tipoEstado;
}
