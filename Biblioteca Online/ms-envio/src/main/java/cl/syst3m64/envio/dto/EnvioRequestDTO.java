package cl.syst3m64.envio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para registrar un nuevo envío")
public class EnvioRequestDTO {

    @Schema(description = "ID de la venta asociada al envío", example = "1")
    @NotNull(message = "El ID de la venta es obligatorio")
    private Long idVenta;

    @Schema(description = "ID de la dirección de destino del envío", example = "5")
    @NotNull(message = "El ID de la dirección es obligatorio")
    private Long idDireccion;

    @Schema(description = "Número de seguimiento del envío", example = "TRACK-2024-00123")
    @NotBlank(message = "El número de seguimiento es obligatorio")
    private String numeroSeguimiento;

    @Schema(description = "Nombre de la empresa de transporte", example = "Chilexpress")
    @NotBlank(message = "La empresa de envío es obligatoria")
    private String empresaEnvio;

    @Schema(description = "Fecha de despacho del envío", example = "2024-12-20")
    @NotBlank(message = "La fecha de despacho es obligatoria")
    private String fechaDespacho;
}
