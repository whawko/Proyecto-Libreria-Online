package cl.syst3m64.libros.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroRequestDTO {

    @NotBlank(message="El titulo es obligatorio")
    private String titulo;

    @NotBlank(message="La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message="El autor es obligatorio")
    private String autor;

    @NotBlank(message="El isbn es obligatorio")
    private String isbn;

    @Min(value=1,message="El precio debe ser mayor a 1")
    private float precio;

    @NotBlank(message="El año es obligatorio")
    private String annio;

    @NotBlank(message="La editorial es obligatoria")
    private String editorial;

    @NotNull(message="El id de la categoria es obligatorio")
    private Long idCategoria;

    @NotNull(message="El id de el estado es obligatorio")
    private Long idEstado;


}
