package cl.syst3m64.venta.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.syst3m64.venta.dto.DetalleRequestDTO;
import cl.syst3m64.venta.dto.DetalleResponseDTO;
import cl.syst3m64.venta.exception.RecursoNoEncontradoException;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;
import cl.syst3m64.venta.service.DetalleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleServiceImpl implements DetalleService {

    private final DetalleRepository detalleRepository;
    private final VentaRepository ventaRepository;

    private DetalleResponseDTO mapToDTO(Detalle detalle) {
        if (detalle == null) return null;
        Long idVenta = (detalle.getIdVenta() != null) ? detalle.getIdVenta().getId() : null;
        return new DetalleResponseDTO(
            detalle.getId(), detalle.getCantidad(), detalle.getSubtotal(),
            idVenta, detalle.getIdLibro()
        );
    }

    @Override
    public List<DetalleResponseDTO> traerTodosLosDetalles() {
        return detalleRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<DetalleResponseDTO> traerDetallePorId(Long id) {
        return detalleRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public DetalleResponseDTO crearDetalle(DetalleRequestDTO detalleDto, Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
            .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + idVenta));
        
        Detalle detalle = new Detalle();
        detalle.setCantidad(detalleDto.getCantidad());
        detalle.setSubtotal(detalleDto.getSubtotal());
        detalle.setIdLibro(detalleDto.getIdLibro());
        detalle.setIdVenta(venta);
        
        return mapToDTO(detalleRepository.save(detalle));
    }

    @Override
    public DetalleResponseDTO actualizarDetalle(Long id, DetalleRequestDTO detalleDto) {
        return detalleRepository.findById(id).map(existing -> {
            existing.setCantidad(detalleDto.getCantidad());
            existing.setSubtotal(detalleDto.getSubtotal());
            existing.setIdLibro(detalleDto.getIdLibro());
            return mapToDTO(detalleRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Detalle no encontrado con ID: " + id));
    }

    @Override
    public void eliminarDetalle(Long id) {
        detalleRepository.deleteById(id);
    }
}
