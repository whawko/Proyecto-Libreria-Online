package cl.syst3m64.direccion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String calle;

    @Column(nullable = false, length = 5)
    private Integer numero;
    
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_comuna", nullable = false)
    private Comuna idComuna;

    @Column(name = "id_estado", nullable = false)
    private Long idEstado;

}
