package cl.syst3m64.direccion.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.direccion.dto.DireccionRequestDTO;
import cl.syst3m64.direccion.dto.DireccionResponseDTO;

public interface DireccionService {
    List<DireccionResponseDTO> obtenerDirecciones();
    Optional<DireccionResponseDTO> obtenerDireccionPorId(Long id);
    DireccionResponseDTO crearDireccion(DireccionRequestDTO direccion);
    DireccionResponseDTO actualizarDireccion(Long id, DireccionRequestDTO direccion);
    void eliminarDireccion(Long id);
    void validarUsuario(Long idUsuario);
    void validarEstado(Long idEstado);
}
