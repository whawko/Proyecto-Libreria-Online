package cl.syst3m64.direccion.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.direccion.dto.ComunaResponseDTO;
import cl.syst3m64.direccion.dto.DireccionRequestDTO;
import cl.syst3m64.direccion.dto.DireccionResponseDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.client.EstadoFeignClient;
import cl.syst3m64.direccion.client.UsuarioFeignClient;
import cl.syst3m64.direccion.exception.RecursoNoEncontradoException;
import cl.syst3m64.direccion.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.DireccionRepository;
import cl.syst3m64.direccion.service.DireccionService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final ComunaRepository comunaRepository;
    private final UsuarioFeignClient usuarioFeignClient;
    private final EstadoFeignClient estadoFeignClient;

    @Override
    public void validarUsuario(Long idUsuario) {
        try {
            usuarioFeignClient.obtenerUsuarioPorId(idUsuario);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El usuario con ID: " + idUsuario + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_usuario: " + e.getMessage());
        }
    }

    @Override
    public void validarEstado(Long idEstado) {
        try {
            estadoFeignClient.obtenerEstadoPorId(idEstado);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El estado con ID: " + idEstado + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    @Override
    public List<DireccionResponseDTO> obtenerDirecciones() {
        return direccionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DireccionResponseDTO> obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Override
    public DireccionResponseDTO crearDireccion(DireccionRequestDTO direccionDto) {
        validarUsuario(direccionDto.getIdUsuario());
        validarEstado(direccionDto.getIdEstado());

        Comuna comuna = comunaRepository.findById(direccionDto.getIdComuna())
                .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada con ID: " + direccionDto.getIdComuna()));

        Direccion direccion = new Direccion();
        direccion.setCalle(direccionDto.getCalle());
        direccion.setNumero(direccionDto.getNumero());
        direccion.setIdUsuario(direccionDto.getIdUsuario());
        direccion.setIdComuna(comuna);
        direccion.setIdEstado(direccionDto.getIdEstado());

        return mapToResponseDTO(direccionRepository.save(direccion));
    }

    @Override
    public DireccionResponseDTO actualizarDireccion(Long id, DireccionRequestDTO direccionDto) {
        validarUsuario(direccionDto.getIdUsuario());
        validarEstado(direccionDto.getIdEstado());

        return direccionRepository.findById(id).map(existing -> {
            Comuna comuna = comunaRepository.findById(direccionDto.getIdComuna())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada con ID: " + direccionDto.getIdComuna()));
            existing.setCalle(direccionDto.getCalle());
            existing.setNumero(direccionDto.getNumero());
            existing.setIdUsuario(direccionDto.getIdUsuario());
            existing.setIdComuna(comuna);
            existing.setIdEstado(direccionDto.getIdEstado());
            return mapToResponseDTO(direccionRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Direccion no encontrada con ID: " + id));
    }

    @Override
    public void eliminarDireccion(Long id) {
        direccionRepository.deleteById(id);
    }

    private DireccionResponseDTO mapToResponseDTO(Direccion direccion) {
        if (direccion == null) return null;

        ComunaResponseDTO comunaDTO = null;
        if (direccion.getIdComuna() != null) {
            Comuna comuna = direccion.getIdComuna();
            RegionResponseDTO regionDTO = null;
            if (comuna.getIdRegion() != null) {
                regionDTO = new RegionResponseDTO(comuna.getIdRegion().getId(), comuna.getIdRegion().getNombre());
            }
            comunaDTO = new ComunaResponseDTO(comuna.getId(), comuna.getNombre(), regionDTO);
        }

        return new DireccionResponseDTO(
                direccion.getId(),
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getIdUsuario(),
                comunaDTO,
                direccion.getIdEstado()
        );
    }
}
