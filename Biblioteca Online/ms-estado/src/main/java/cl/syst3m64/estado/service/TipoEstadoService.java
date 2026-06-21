package cl.syst3m64.estado.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.estado.dto.TipoEstadoRequestDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;

public interface TipoEstadoService {
    List<TipoEstadoResponseDTO> obtenerTipoEstados();
    Optional<TipoEstadoResponseDTO> obtenerTipoEstadosPorId(Long idTipo);
    TipoEstadoResponseDTO guardarTipoEstado(TipoEstadoRequestDTO tipoEstadoDto);
    TipoEstadoResponseDTO actualizarTipoEstado(Long id, TipoEstadoRequestDTO tipoEstadoDto);
    void eliminarTipoEstado(Long idTipo);
}
