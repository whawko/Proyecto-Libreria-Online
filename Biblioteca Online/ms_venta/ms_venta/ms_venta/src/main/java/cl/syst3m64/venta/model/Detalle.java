package cl.syst3m64.venta.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "detalles")
public class Detalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =  10)
    private Integer cantidad;

    @Column(nullable = false, length = 10)
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta idVenta;

    @Column(name = "id_libro", nullable = false)
    private Long idLibro;
}
