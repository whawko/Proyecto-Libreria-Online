package cl.syst3m64.categorias.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.categorias.dto.CategoriaRequestDTO;
import cl.syst3m64.categorias.dto.CategoriaResponseDTO;

public interface CategoriaService {
    List<CategoriaResponseDTO> obtenerTodas();
    List<CategoriaResponseDTO> obtenerNombres(String nombre);
    CategoriaResponseDTO guardar(CategoriaRequestDTO cat);
    Optional<CategoriaResponseDTO> obtenerPorId(Long id);
    Optional<CategoriaResponseDTO> actualizar(Long id, CategoriaRequestDTO cat);
    void eliminar(Long id);
}

