package cl.syst3m64.venta.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.syst3m64.venta.dto.CarritoItemDTO;
import cl.syst3m64.venta.dto.VentaRequestDTO;
import cl.syst3m64.venta.dto.VentaResponseDTO;
import cl.syst3m64.venta.client.CarritoFeignClient;
import cl.syst3m64.venta.client.EstadoFeignClient;
import cl.syst3m64.venta.client.UsuarioFeignClient;
import cl.syst3m64.venta.exception.RecursoNoEncontradoException;
import cl.syst3m64.venta.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.venta.model.Detalle;
import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.DetalleRepository;
import cl.syst3m64.venta.repository.VentaRepository;
import cl.syst3m64.venta.service.VentaService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final EstadoFeignClient estadoFeignClient;
    private final UsuarioFeignClient usuarioFeignClient;
    private final CarritoFeignClient carritoFeignClient;
    private final DetalleRepository detalleRepository;

    private VentaResponseDTO mapToDTO(Venta venta) {
        return new VentaResponseDTO(
            venta.getId(), venta.getFecha(), venta.getTotal(),
            venta.getIdEstado(), venta.getIdUsuario()
        );
    }

    @Override
    public void validarEstado(Long estadoId) {
        try {
            estadoFeignClient.obtenerEstadoPorId(estadoId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El estado con ID: " + estadoId + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    @Override
    public void validarUsuario(Long usuarioId) {
        try {
            usuarioFeignClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El usuario con ID: " + usuarioId + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_usuario: " + e.getMessage());
        }
    }

    @Override
    public List<VentaResponseDTO> traerTodasLasVentas() {
        return ventaRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public Optional<VentaResponseDTO> traerVentasPorId(Long idVenta) {
        return ventaRepository.findById(idVenta).map(this::mapToDTO);
    }

    @Override
    public Optional<Venta> buscarVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public VentaResponseDTO crearVenta(VentaRequestDTO dto) {
        validarEstado(dto.getIdEstado());
        validarUsuario(dto.getIdUsuario());
        Venta venta = new Venta(null, dto.getFecha(), dto.getTotal(), dto.getIdEstado(), dto.getIdUsuario());
        return mapToDTO(ventaRepository.save(venta));
    }

    @Override
    public VentaResponseDTO actualizarVenta(Long id, VentaRequestDTO dto) {
        validarEstado(dto.getIdEstado());
        validarUsuario(dto.getIdUsuario());
        return ventaRepository.findById(id).map(existing -> {
            existing.setFecha(dto.getFecha());
            existing.setTotal(dto.getTotal());
            existing.setIdEstado(dto.getIdEstado());
            existing.setIdUsuario(dto.getIdUsuario());
            return mapToDTO(ventaRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    @Override
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public VentaResponseDTO crearVentaDesdeCarrito(Long idUsuario) {
        // 1. Validar que el usuario exista
        validarUsuario(idUsuario);

        // 2. Obtener los items del carrito
        List<CarritoItemDTO> items;
        try {
            items = carritoFeignClient.obtenerCarritoPorUsuario(idUsuario);
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-carrito: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío, no se puede generar la venta");
        }

        // 3. Calcular el total de la venta
        BigDecimal total = items.stream()
                .map(CarritoItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Crear la venta con estado PENDIENTE (usamos 1L que es ACTIVO)
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Venta venta = new Venta(null, fechaActual, total, 1L, idUsuario);
        Venta ventaGuardada = ventaRepository.save(venta);

        // 5. Crear los detalles de la venta
        for (CarritoItemDTO item : items) {
            Detalle detalle = new Detalle(null, item.getCantidad(), item.getSubtotal(), ventaGuardada, item.getIdLibro());
            detalleRepository.save(detalle);
        }

        // 6. Vaciar el carrito
        try {
            carritoFeignClient.vaciarCarrito(idUsuario);
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se pudo vaciar el carrito en el microservicio ms-carrito: " + e.getMessage());
        }

        return mapToDTO(ventaGuardada);
    }

    @Override
    @Transactional
    public VentaResponseDTO actualizarEstadoVenta(Long id, Long idEstado) {
        validarEstado(idEstado);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        venta.setIdEstado(idEstado);
        return mapToDTO(ventaRepository.save(venta));
    }
}
