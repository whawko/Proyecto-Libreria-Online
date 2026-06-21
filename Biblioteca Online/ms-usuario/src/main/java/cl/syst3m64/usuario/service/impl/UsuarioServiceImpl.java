package cl.syst3m64.usuario.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.usuario.dto.UsuarioRequestDTO;
import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.client.EstadoFeignClient;
import cl.syst3m64.usuario.exception.RecursoNoEncontradoException;
import cl.syst3m64.usuario.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.repository.UsuarioRepository;
import cl.syst3m64.usuario.service.UsuarioService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoFeignClient estadoFeignClient;

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(), usuario.getRut(), usuario.getNombres(), usuario.getApellidos(),
            usuario.getFechaNacimiento(), usuario.getCorreo(), usuario.getClave(),
            usuario.getIdRol(), usuario.getIdEstado()
        );
    }

    @Override
    public void validarEstado(Long estadoId) {
        try {
            estadoFeignClient.obtenerEstadoPorId(estadoId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El estado con ID: " + estadoId + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioResponseDTO> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        validarEstado(request.getIdEstado());
        Rol rol = rolRepository.findById(request.getIdRol())
            .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getIdRol()));

        Usuario usuario = new Usuario(
            null,
            request.getRut(),
            request.getNombres(),
            request.getApellidos(),
            request.getFechaNacimiento(),
            request.getCorreo(),
            request.getClave(),
            rol,
            request.getIdEstado()
        );
        return mapToDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        validarEstado(request.getIdEstado());
        Rol rol = rolRepository.findById(request.getIdRol())
            .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con ID: " + request.getIdRol()));

        return usuarioRepository.findById(id).map(existing -> {
            existing.setRut(request.getRut());
            existing.setNombres(request.getNombres());
            existing.setApellidos(request.getApellidos());
            existing.setFechaNacimiento(request.getFechaNacimiento());
            existing.setCorreo(request.getCorreo());
            existing.setClave(request.getClave());
            existing.setIdRol(rol);
            existing.setIdEstado(request.getIdEstado());
            return mapToDTO(usuarioRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}

