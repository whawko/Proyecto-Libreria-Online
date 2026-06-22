package cl.syst3m64.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de transporte para datos básicos del libro en ms-carrito")
public class LibroResponseDTO {

    @Schema(description = "ID único del libro", example = "10")
    private Long id;

    @Schema(description = "Título del libro", example = "El ingenioso hidalgo Don Quijote de la Mancha")
    private String titulo;

    @Schema(description = "Precio del libro", example = "15000.0")
    private float precio;
}
