package cl.syst3m64.direccion.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.direccion.dto.RegionRequestDTO;
import cl.syst3m64.direccion.dto.RegionResponseDTO;

public interface RegionService {
    List<RegionResponseDTO> obtenerRegiones();
    Optional<RegionResponseDTO> obtenerRegionPorId(Long id);
    RegionResponseDTO crearRegion(RegionRequestDTO region);
    RegionResponseDTO actualizarRegion(Long id, RegionRequestDTO region);
    void eliminarRegion(Long id);
}
