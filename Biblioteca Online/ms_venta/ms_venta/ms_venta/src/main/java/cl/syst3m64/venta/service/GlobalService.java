package cl.syst3m64.venta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.feign.EstadoFeignClient;
import cl.syst3m64.venta.feign.UsuarioFeignClient;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private final VentaRepository ventaRepository;
    private final DetalleRepository detalleRepository;
    private final EstadoFeignClient estadoFeignClient;
    private final UsuarioFeignClient usuarioFeignClient;

    private VentaResponseDTO mapToDTO(Venta venta) {
        return new VentaResponseDTO(
            venta.getId(), venta.getFecha(), venta.getTotal(),
            venta.getIdEstado(), venta.getIdUsuario()
        );
    }

    public void validarEstado(Long estadoId) {
        try {
            estadoFeignClient.obtenerEstadoPorId(estadoId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El estado con ID: " + estadoId + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    public void validarUsuario(Long usuarioId) {
        try {
            usuarioFeignClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El usuario con ID: " + usuarioId + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con el microservicio ms_usuario: " + e.getMessage());
        }
    }

    public List<Venta> traerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public Optional<VentaResponseDTO> traerVentasPorId(Long idVenta) {
        return ventaRepository.findById(idVenta).map(this::mapToDTO);
    }

    public Optional<Venta> buscarVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public VentaResponseDTO crearVenta(VentaRequestDTO dto) {
        validarEstado(dto.getIdEstado());
        validarUsuario(dto.getIdUsuario());
        Venta venta = new Venta(null, dto.getFecha(), dto.getTotal(), dto.getIdEstado(), dto.getIdUsuario());
        return mapToDTO(ventaRepository.save(venta));
    }

    public VentaResponseDTO actualizarVenta(Long id, VentaRequestDTO dto) {
        validarEstado(dto.getIdEstado());
        validarUsuario(dto.getIdUsuario());
        return ventaRepository.findById(id).map(existing -> {
            existing.setFecha(dto.getFecha());
            existing.setTotal(dto.getTotal());
            existing.setIdEstado(dto.getIdEstado());
            existing.setIdUsuario(dto.getIdUsuario());
            return mapToDTO(ventaRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    public List<Detalle> traerTodosLosDetalles() {
        return detalleRepository.findAll();
    }

    public Optional<Detalle> traerDetallePorId(Long id) {
        return detalleRepository.findById(id);
    }

    public Detalle crearDetalle(Detalle detalle, Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));
        detalle.setIdVenta(venta);
        return detalleRepository.save(detalle);
    }

    public Detalle actualizarDetalle(Long id, Detalle detalle) {
        return detalleRepository.findById(id).map(existing -> {
            existing.setCantidad(detalle.getCantidad());
            existing.setSubtotal(detalle.getSubtotal());
            existing.setIdLibro(detalle.getIdLibro());
            return detalleRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Detalle no encontrado con ID: " + id));
    }

    public void eliminarDetalle(Long id) {
        detalleRepository.deleteById(id);
    }
}
