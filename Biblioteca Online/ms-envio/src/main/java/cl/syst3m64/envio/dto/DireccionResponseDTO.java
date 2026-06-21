package cl.syst3m64.envio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionResponseDTO {
    private Long id;
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
}
