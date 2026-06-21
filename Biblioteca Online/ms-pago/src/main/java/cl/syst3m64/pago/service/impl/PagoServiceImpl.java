package cl.syst3m64.pago.service.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.pago.dto.PagoRequestDTO;
import cl.syst3m64.pago.dto.PagoResponseDTO;
import cl.syst3m64.pago.dto.VentaResponseDTO;
import cl.syst3m64.pago.client.VentaFeignClient;
import cl.syst3m64.pago.exception.RecursoNoEncontradoException;
import cl.syst3m64.pago.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.pago.model.Pago;
import cl.syst3m64.pago.repository.PagoRepository;
import cl.syst3m64.pago.service.PagoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final VentaFeignClient ventaFeignClient;

    @Override
    @Transactional
    public PagoResponseDTO registrarPago(PagoRequestDTO request) {
        // 1. Validar que la venta exista en ms_venta
        VentaResponseDTO venta;
        try {
            venta = ventaFeignClient.obtenerVentaPorId(request.getIdVenta());
            if (venta == null) {
                throw new RecursoNoEncontradoException("La venta con ID " + request.getIdVenta() + " no fue encontrada.");
            }
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("La venta con ID " + request.getIdVenta() + " no existe.");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-venta: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la venta con ID " + request.getIdVenta() + ": " + e.getMessage());
        }

        // 2. Validar que el monto del pago coincida con el total de la venta
        if (request.getMonto().compareTo(venta.getTotal()) != 0) {
            throw new IllegalArgumentException("El monto de pago (" + request.getMonto() + ") no coincide con el total de la venta (" + venta.getTotal() + ").");
        }

        // 3. Verificar si ya existe un pago para esta venta
        Optional<Pago> pagoExistente = pagoRepository.findByIdVenta(request.getIdVenta());
        if (pagoExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un pago registrado para la venta con ID: " + request.getIdVenta());
        }

        // 4. Crear y guardar el pago
        Pago pago = new Pago();
        pago.setIdVenta(request.getIdVenta());
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setTransaccionId(request.getTransaccionId());
        pago.setFechaPago(request.getFechaPago());
        pago.setIdEstado(1L); // 1L represents 'APPROVED' or 'SUCCESS' payment state

        Pago saved = pagoRepository.save(pago);

        // 5. Actualizar el estado de la venta a PAGADA (3L)
        try {
            ventaFeignClient.actualizarEstadoVenta(request.getIdVenta(), 3L);
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se pudo actualizar el estado de la venta en el microservicio ms-venta: " + e.getMessage());
        }

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoResponseDTO> obtenerPagoPorVenta(Long idVenta) {
        return pagoRepository.findByIdVenta(idVenta).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoResponseDTO> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id).map(this::mapToResponse);
    }

    private PagoResponseDTO mapToResponse(Pago pago) {
        return new PagoResponseDTO(
            pago.getId(),
            pago.getIdVenta(),
            pago.getMonto(),
            pago.getMetodoPago(),
            pago.getTransaccionId(),
            pago.getFechaPago(),
            pago.getIdEstado()
        );
    }
}
