package cl.syst3m64.envio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un envío")
public class EnvioResponseDTO {
    @Schema(description = "ID único del envío", example = "1")
    private Long id;

    @Schema(description = "ID de la venta asociada", example = "1")
    private Long idVenta;

    @Schema(description = "ID de la dirección de destino", example = "5")
    private Long idDireccion;

    @Schema(description = "Número de seguimiento del envío", example = "TRACK-2024-00123")
    private String numeroSeguimiento;

    @Schema(description = "Nombre de la empresa de transporte", example = "Chilexpress")
    private String empresaEnvio;

    @Schema(description = "Fecha de despacho del envío", example = "2024-12-20")
    private String fechaDespacho;

    @Schema(description = "ID del estado actual del envío", example = "3")
    private Long idEstado;
}
