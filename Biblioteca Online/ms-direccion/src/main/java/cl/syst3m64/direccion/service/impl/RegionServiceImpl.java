package cl.syst3m64.direccion.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.direccion.dto.RegionRequestDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;
import cl.syst3m64.direccion.exception.RecursoNoEncontradoException;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.RegionRepository;
import cl.syst3m64.direccion.service.RegionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public List<RegionResponseDTO> obtenerRegiones() {
        return regionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RegionResponseDTO> obtenerRegionPorId(Long id) {
        return regionRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Override
    public RegionResponseDTO crearRegion(RegionRequestDTO regionDto) {
        Region region = new Region();
        region.setNombre(regionDto.getNombre());
        return mapToResponseDTO(regionRepository.save(region));
    }

    @Override
    public RegionResponseDTO actualizarRegion(Long id, RegionRequestDTO regionDto) {
        return regionRepository.findById(id).map(existing -> {
            existing.setNombre(regionDto.getNombre());
            return mapToResponseDTO(regionRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Region no encontrada con ID: " + id));
    }

    @Override
    public void eliminarRegion(Long id) {
        regionRepository.deleteById(id);
    }

    private RegionResponseDTO mapToResponseDTO(Region region) {
        if (region == null) return null;
        return new RegionResponseDTO(region.getId(), region.getNombre());
    }
}
