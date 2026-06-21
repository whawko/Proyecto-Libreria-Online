package cl.syst3m64.libros.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.libros.dto.LibroRequestDTO;
import cl.syst3m64.libros.dto.LibroResponseDTO;

public interface LibroService {
    void validarCategoria(Long categoriaId);
    void validarEstado(Long estadoId);
    List<LibroResponseDTO> obtenerLibros();
    Optional<LibroResponseDTO> obtenerPorId(Long id);
    LibroResponseDTO guardarLibro(LibroRequestDTO libro);
    LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libro);
    void eliminarLibro(Long id);
}

