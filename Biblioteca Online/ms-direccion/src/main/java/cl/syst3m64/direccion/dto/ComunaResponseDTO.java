package cl.syst3m64.direccion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunaResponseDTO {
    private Long id;
    private String nombre;
    private RegionResponseDTO region;
}
