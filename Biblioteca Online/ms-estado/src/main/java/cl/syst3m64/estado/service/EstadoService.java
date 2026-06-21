package cl.syst3m64.estado.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.estado.dto.EstadoRequestDTO;
import cl.syst3m64.estado.dto.EstadoResponseDTO;

public interface EstadoService {
    List<EstadoResponseDTO> obtenerTodosEstados();
    Optional<EstadoResponseDTO> obtenerEstadoPorId(Long id);
    EstadoResponseDTO guardarEstado(EstadoRequestDTO estadoDto);
    EstadoResponseDTO actualizarEstado(Long id, EstadoRequestDTO estadoDto);
    void eliminarEstado(Long id);
}
