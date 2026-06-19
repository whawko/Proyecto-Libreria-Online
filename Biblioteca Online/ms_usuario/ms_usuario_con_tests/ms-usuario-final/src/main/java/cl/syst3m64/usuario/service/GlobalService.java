package cl.syst3m64.usuario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.usuario.dto.UsuarioResponseDTO;
import cl.syst3m64.usuario.feign.EstadoFeignClient;
import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.repository.UsuarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoFeignClient estadoFeignClient;

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(), usuario.getRut(), usuario.getNombres(), usuario.getApellidos(),
            usuario.getFecha_nacimiento(), usuario.getCorreo(), usuario.getClave(),
            usuario.getIdRol(), usuario.getIdEstado()
        );
    }

    public void validarEstado(Long estadoId) {
        try {
            estadoFeignClient.obtenerEstadoPorId(estadoId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El estado con ID: " + estadoId + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    public UsuarioResponseDTO crearUsuario(Usuario usuario) {
        validarEstado(usuario.getIdEstado());
        return mapToDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO actualizarUsuario(Long id, Usuario usuario) {
        validarEstado(usuario.getIdEstado());
        return usuarioRepository.findById(id).map(existing -> {
            existing.setRut(usuario.getRut());
            existing.setNombres(usuario.getNombres());
            existing.setApellidos(usuario.getApellidos());
            existing.setFecha_nacimiento(usuario.getFecha_nacimiento());
            existing.setCorreo(usuario.getCorreo());
            existing.setClave(usuario.getClave());
            existing.setIdRol(usuario.getIdRol());
            existing.setIdEstado(usuario.getIdEstado());
            return mapToDTO(usuarioRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    public Rol crearRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol actualizarRol(Long id, Rol rol) {
        return rolRepository.findById(id).map(existing -> {
            existing.setNombre(rol.getNombre());
            return rolRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}
