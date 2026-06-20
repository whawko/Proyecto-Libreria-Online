package cl.syst3m64.venta.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {
    private String fecha;
    private BigDecimal total;
    private Long idEstado;
    private Long idUsuario;
}
