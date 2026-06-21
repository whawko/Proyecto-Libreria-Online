package cl.syst3m64.direccion.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.direccion.dto.ComunaRequestDTO;
import cl.syst3m64.direccion.dto.ComunaResponseDTO;

public interface ComunaService {
    List<ComunaResponseDTO> obtenerComunas();
    Optional<ComunaResponseDTO> obtenerComunaPorId(Long id);
    ComunaResponseDTO crearComuna(ComunaRequestDTO comuna);
    ComunaResponseDTO actualizarComuna(Long id, ComunaRequestDTO comuna);
    void eliminarComuna(Long id);
}
