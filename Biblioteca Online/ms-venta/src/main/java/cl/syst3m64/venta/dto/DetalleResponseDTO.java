package cl.syst3m64.venta.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleResponseDTO {
    private Long id;
    private Integer cantidad;
    private BigDecimal subtotal;
    private Long idVenta;
    private Long idLibro;
}
