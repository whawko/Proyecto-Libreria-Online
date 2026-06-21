package cl.syst3m64.pago.service;

import java.util.Optional;
import cl.syst3m64.pago.dto.PagoRequestDTO;
import cl.syst3m64.pago.dto.PagoResponseDTO;

public interface PagoService {
    PagoResponseDTO registrarPago(PagoRequestDTO request);
    Optional<PagoResponseDTO> obtenerPagoPorVenta(Long idVenta);
    Optional<PagoResponseDTO> obtenerPagoPorId(Long id);
}

