package cl.syst3m64.venta.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.venta.dto.DetalleRequestDTO;
import cl.syst3m64.venta.dto.DetalleResponseDTO;

public interface DetalleService {
    List<DetalleResponseDTO> traerTodosLosDetalles();
    Optional<DetalleResponseDTO> traerDetallePorId(Long id);
    DetalleResponseDTO crearDetalle(DetalleRequestDTO detalle, Long idVenta);
    DetalleResponseDTO actualizarDetalle(Long id, DetalleRequestDTO detalle);
    void eliminarDetalle(Long id);
}
