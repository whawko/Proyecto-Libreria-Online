package cl.syst3m64.envio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_venta", nullable = false)
    private Long idVenta;

    @Column(name = "id_direccion", nullable = false)
    private Long idDireccion;

    @Column(name = "numero_seguimiento", nullable = false, length = 50)
    private String numeroSeguimiento;

    @Column(name = "empresa_envio", nullable = false, length = 30)
    private String empresaEnvio;

    @Column(name = "fecha_despacho", nullable = false, length = 30)
    private String fechaDespacho;

    @Column(name = "id_estado", nullable = false)
    private Long idEstado;
}
