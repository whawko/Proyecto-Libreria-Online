package cl.syst3m64.libros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String autor;
    private String isbn;
    private float precio;
    private String annio;
    private String editorial;
    private Long idCategoria;
    private Long idEstado;

}
