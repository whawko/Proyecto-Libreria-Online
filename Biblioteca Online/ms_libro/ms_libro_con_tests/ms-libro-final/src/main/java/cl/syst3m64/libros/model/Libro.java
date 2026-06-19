package cl.syst3m64.libros.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,length=50)
    private String titulo;

    @Column(nullable=false,length=50)
    private String descripcion;

    @Column(nullable=false,length=30)
    private String autor;

    @Column(nullable=false,length=10)
    private String isbn;

    @Column(nullable=false,length=9)
    private float precio;

    @Column(nullable=false,length=10)
    private String annio;

    @Column(nullable=false,length=50)
    private String editorial;
    
    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "id_estado", nullable = false)
    private Long idEstado;
}
