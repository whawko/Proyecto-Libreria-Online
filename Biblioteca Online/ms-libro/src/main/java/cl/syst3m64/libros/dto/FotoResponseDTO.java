package cl.syst3m64.libros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoResponseDTO {
    private Long id;
    private String url;
    private String nombre;
    private String portada;
    private Long idLibro;
}
