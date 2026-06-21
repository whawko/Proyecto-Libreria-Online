package cl.syst3m64.resena.service;

import cl.syst3m64.resena.dto.ResenaRequestDTO;
import cl.syst3m64.resena.dto.ResenaResponseDTO;
import java.util.List;

public interface ResenaService {
    ResenaResponseDTO crear(ResenaRequestDTO dto);
    List<ResenaResponseDTO> listarTodas();
    ResenaResponseDTO buscarPorId(Long id);
    List<ResenaResponseDTO> listarPorLibro(Long idLibro);
    List<ResenaResponseDTO> listarPorUsuario(Long idUsuario);
    ResenaResponseDTO actualizar(Long id, ResenaRequestDTO dto);
    void eliminar(Long id);
}


