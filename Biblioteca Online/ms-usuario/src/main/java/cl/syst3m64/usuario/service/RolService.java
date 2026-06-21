package cl.syst3m64.usuario.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.usuario.dto.RolRequestDTO;
import cl.syst3m64.usuario.dto.RolResponseDTO;

public interface RolService {
    List<RolResponseDTO> obtenerTodosLosRoles();
    Optional<RolResponseDTO> obtenerRolPorId(Long id);
    RolResponseDTO crearRol(RolRequestDTO rol);
    RolResponseDTO actualizarRol(Long id, RolRequestDTO rol);
    void eliminarRol(Long id);
}

