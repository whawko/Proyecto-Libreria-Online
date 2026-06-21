package cl.syst3m64.envio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponseDTO {
    private Long id;
    private Long idVenta;
    private Long idDireccion;
    private String numeroSeguimiento;
    private String empresaEnvio;
    private String fechaDespacho;
    private Long idEstado;
}
