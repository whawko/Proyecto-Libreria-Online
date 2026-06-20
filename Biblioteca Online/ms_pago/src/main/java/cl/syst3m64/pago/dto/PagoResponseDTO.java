package cl.syst3m64.pago.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {
    private Long id;
    private Long idVenta;
    private BigDecimal monto;
    private String metodoPago;
    private String transaccionId;
    private String fechaPago;
    private Long idEstado;
}
