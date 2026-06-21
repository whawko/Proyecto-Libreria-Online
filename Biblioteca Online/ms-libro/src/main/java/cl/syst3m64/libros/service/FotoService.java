package cl.syst3m64.libros.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.libros.dto.FotoRequestDTO;
import cl.syst3m64.libros.dto.FotoResponseDTO;
import cl.syst3m64.libros.model.Libro;

public interface FotoService {
    Libro obtenerLibro(Long idLibro);
    List<FotoResponseDTO> obtenerFotos();
    Optional<FotoResponseDTO> obtenerPorId(Long id);
    FotoResponseDTO guardarFoto(FotoRequestDTO foto, Long idLibro);
    FotoResponseDTO actualizarFoto(Long id, FotoRequestDTO foto);
    void eliminarFoto(Long id);
}