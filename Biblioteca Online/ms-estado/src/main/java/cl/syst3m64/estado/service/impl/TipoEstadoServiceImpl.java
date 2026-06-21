package cl.syst3m64.estado.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.estado.dto.TipoEstadoRequestDTO;
import cl.syst3m64.estado.dto.TipoEstadoResponseDTO;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import cl.syst3m64.estado.service.TipoEstadoService;
import cl.syst3m64.estado.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoEstadoServiceImpl implements TipoEstadoService {

    private final TipoEstadoRepository tipoEstadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoEstadoResponseDTO> obtenerTipoEstados() {
        return tipoEstadoRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoEstadoResponseDTO> obtenerTipoEstadosPorId(Long idTipo) {
        return tipoEstadoRepository.findById(idTipo).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public TipoEstadoResponseDTO guardarTipoEstado(TipoEstadoRequestDTO tipoEstadoDto) {
        TipoEstado tipoEstado = new TipoEstado();
        tipoEstado.setNombre(tipoEstadoDto.getNombre());
        TipoEstado saved = tipoEstadoRepository.save(tipoEstado);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TipoEstadoResponseDTO actualizarTipoEstado(Long id, TipoEstadoRequestDTO tipoEstadoDto) {
        TipoEstado existing = tipoEstadoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("TipoEstado no encontrado con ID: " + id));
        existing.setNombre(tipoEstadoDto.getNombre());
        TipoEstado saved = tipoEstadoRepository.save(existing);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void eliminarTipoEstado(Long idTipo) {
        if (!tipoEstadoRepository.existsById(idTipo)) {
            throw new RecursoNoEncontradoException("TipoEstado no encontrado con ID: " + idTipo);
        }
        tipoEstadoRepository.deleteById(idTipo);
    }

    private TipoEstadoResponseDTO mapToResponse(TipoEstado tipoEstado) {
        return new TipoEstadoResponseDTO(
            tipoEstado.getId(),
            tipoEstado.getNombre()
        );
    }
}
