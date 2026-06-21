package cl.syst3m64.estado.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.estado.dto.EstadoRequestDTO;
import cl.syst3m64.estado.dto.EstadoResponseDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;
import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.EstadoRepository;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import cl.syst3m64.estado.service.EstadoService;
import cl.syst3m64.estado.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EstadoResponseDTO> obtenerTodosEstados() {
        return estadoRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoResponseDTO> obtenerEstadoPorId(Long id) {
        return estadoRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public EstadoResponseDTO guardarEstado(EstadoRequestDTO estadoDto) {
        TipoEstado tipoEstado = tipoEstadoRepository.findById(estadoDto.getIdTipoEstado())
            .orElseThrow(() -> new RecursoNoEncontradoException("TipoEstado no encontrado con ID: " + estadoDto.getIdTipoEstado()));

        Estado estado = new Estado();
        estado.setNombre(estadoDto.getNombre());
        estado.setDescripcion(estadoDto.getDescripcion());
        estado.setTipoEstado(tipoEstado);

        Estado saved = estadoRepository.save(estado);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public EstadoResponseDTO actualizarEstado(Long id, EstadoRequestDTO estadoDto) {
        Estado existing = estadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + id));

        TipoEstado tipoEstado = tipoEstadoRepository.findById(estadoDto.getIdTipoEstado())
            .orElseThrow(() -> new RecursoNoEncontradoException("TipoEstado no encontrado con ID: " + estadoDto.getIdTipoEstado()));

        existing.setNombre(estadoDto.getNombre());
        existing.setDescripcion(estadoDto.getDescripcion());
        existing.setTipoEstado(tipoEstado);

        Estado saved = estadoRepository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void eliminarEstado(Long id) {
        if (!estadoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estado no encontrado con ID: " + id);
        }
        estadoRepository.deleteById(id);
    }

    private EstadoResponseDTO mapToResponse(Estado estado) {
        TipoEstadoResponseDTO tipoDto = null;
        if (estado.getTipoEstado() != null) {
            tipoDto = new TipoEstadoResponseDTO(
                estado.getTipoEstado().getId(),
                estado.getTipoEstado().getNombre()
            );
        }
        return new EstadoResponseDTO(
            estado.getId(),
            estado.getNombre(),
            estado.getDescripcion(),
            tipoDto
        );
    }
}
