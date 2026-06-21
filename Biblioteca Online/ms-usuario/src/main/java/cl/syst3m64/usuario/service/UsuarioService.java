package cl.syst3m64.usuario.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.usuario.dto.UsuarioRequestDTO;
import cl.syst3m64.usuario.dto.UsuarioResponseDTO;

public interface UsuarioService {
    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    Optional<UsuarioResponseDTO> obtenerUsuarioPorId(Long id);
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request);
    void eliminarUsuario(Long id);
    void validarEstado(Long estadoId);
}

