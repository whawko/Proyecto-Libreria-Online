package cl.syst3m64.direccion.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.direccion.dto.ComunaRequestDTO;
import cl.syst3m64.direccion.dto.ComunaResponseDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.exception.RecursoNoEncontradoException;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.RegionRepository;
import cl.syst3m64.direccion.service.ComunaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComunaServiceImpl implements ComunaService {

    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;

    @Override
    public List<ComunaResponseDTO> obtenerComunas() {
        return comunaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ComunaResponseDTO> obtenerComunaPorId(Long id) {
        return comunaRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Override
    public ComunaResponseDTO crearComuna(ComunaRequestDTO comunaDto) {
        Region region = regionRepository.findById(comunaDto.getIdRegion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Region no encontrada con ID: " + comunaDto.getIdRegion()));

        Comuna comuna = new Comuna();
        comuna.setNombre(comunaDto.getNombre());
        comuna.setIdRegion(region);

        return mapToResponseDTO(comunaRepository.save(comuna));
    }

    @Override
    public ComunaResponseDTO actualizarComuna(Long id, ComunaRequestDTO comunaDto) {
        return comunaRepository.findById(id).map(existing -> {
            Region region = regionRepository.findById(comunaDto.getIdRegion())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Region no encontrada con ID: " + comunaDto.getIdRegion()));
            existing.setNombre(comunaDto.getNombre());
            existing.setIdRegion(region);
            return mapToResponseDTO(comunaRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Comuna no encontrada con ID: " + id));
    }

    @Override
    public void eliminarComuna(Long id) {
        comunaRepository.deleteById(id);
    }

    private ComunaResponseDTO mapToResponseDTO(Comuna comuna) {
        if (comuna == null) return null;
        RegionResponseDTO regionDTO = null;
        if (comuna.getIdRegion() != null) {
            regionDTO = new RegionResponseDTO(comuna.getIdRegion().getId(), comuna.getIdRegion().getNombre());
        }
        return new ComunaResponseDTO(comuna.getId(), comuna.getNombre(), regionDTO);
    }
}
