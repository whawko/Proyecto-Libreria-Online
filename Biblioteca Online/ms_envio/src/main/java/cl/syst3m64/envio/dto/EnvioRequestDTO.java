package cl.syst3m64.envio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioRequestDTO {

    @NotNull(message = "El ID de la venta es obligatorio")
    private Long idVenta;

    @NotNull(message = "El ID de la dirección es obligatorio")
    private Long idDireccion;

    @NotBlank(message = "El número de seguimiento es obligatorio")
    private String numeroSeguimiento;

    @NotBlank(message = "La empresa de envío es obligatoria")
    private String empresaEnvio;

    @NotBlank(message = "La fecha de despacho es obligatoria")
    private String fechaDespacho;
}
