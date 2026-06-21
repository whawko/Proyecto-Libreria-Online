package cl.syst3m64.venta.service;

import java.util.List;
import java.util.Optional;
import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.model.Venta;

public interface VentaService {
    List<VentaResponseDTO> traerTodasLasVentas();
    Optional<VentaResponseDTO> traerVentasPorId(Long idVenta);
    Optional<Venta> buscarVentaPorId(Long id);
    VentaResponseDTO crearVenta(VentaRequestDTO dto);
    VentaResponseDTO actualizarVenta(Long id, VentaRequestDTO dto);
    void eliminarVenta(Long id);
    void validarEstado(Long estadoId);
    void validarUsuario(Long usuarioId);
    VentaResponseDTO crearVentaDesdeCarrito(Long idUsuario);
    VentaResponseDTO actualizarEstadoVenta(Long id, Long idEstado);
}
