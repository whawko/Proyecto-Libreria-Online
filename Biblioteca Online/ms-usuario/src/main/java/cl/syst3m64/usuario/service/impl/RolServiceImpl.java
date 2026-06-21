package cl.syst3m64.usuario.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.syst3m64.usuario.dto.RolRequestDTO;
import cl.syst3m64.usuario.dto.RolResponseDTO;
import cl.syst3m64.usuario.exception.RecursoNoEncontradoException;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.service.RolService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    private RolResponseDTO mapToDTO(Rol rol) {
        return new RolResponseDTO(rol.getId(), rol.getNombre());
    }

    @Override
    public List<RolResponseDTO> obtenerTodosLosRoles() {
        return rolRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<RolResponseDTO> obtenerRolPorId(Long id) {
        return rolRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public RolResponseDTO crearRol(RolRequestDTO rolDto) {
        Rol rol = new Rol(null, rolDto.getNombre());
        return mapToDTO(rolRepository.save(rol));
    }

    @Override
    public RolResponseDTO actualizarRol(Long id, RolRequestDTO rolDto) {
        return rolRepository.findById(id).map(existing -> {
            existing.setNombre(rolDto.getNombre());
            return mapToDTO(rolRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + id));
    }

    @Override
    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}

