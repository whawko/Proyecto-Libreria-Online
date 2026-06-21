package cl.syst3m64.envio.service;

import java.util.Optional;
import cl.syst3m64.envio.dto.EnvioRequestDTO;
import cl.syst3m64.envio.dto.EnvioResponseDTO;

public interface EnvioService {
    EnvioResponseDTO registrarEnvio(EnvioRequestDTO request);
    Optional<EnvioResponseDTO> obtenerEnvioPorVenta(Long idVenta);
    Optional<EnvioResponseDTO> obtenerEnvioPorId(Long id);
}

