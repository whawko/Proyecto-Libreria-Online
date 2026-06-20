package cl.syst3m64.pago.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.pago.dto.PagoRequestDTO;
import cl.syst3m64.pago.dto.PagoResponseDTO;
import cl.syst3m64.pago.dto.VentaResponseDTO;
import cl.syst3m64.pago.feign.VentaFeignClient;
import cl.syst3m64.pago.model.Pago;
import cl.syst3m64.pago.repository.PagoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final VentaFeignClient ventaFeignClient;

    @Transactional
    public PagoResponseDTO registrarPago(PagoRequestDTO request) {
        // 1. Validar que la venta exista en ms_venta
        VentaResponseDTO venta;
        try {
            venta = ventaFeignClient.obtenerVentaPorId(request.getIdVenta());
            if (venta == null) {
                throw new RuntimeException("La venta con ID " + request.getIdVenta() + " no fue encontrada.");
            }
        } catch (Exception e) {
            throw new RuntimeException("La venta con ID " + request.getIdVenta() + " no existe o no pudo ser validada. Error: " + e.getMessage());
        }

        // 2. Validar que el monto del pago coincida con el total de la venta
        if (request.getMonto().compareTo(venta.getTotal()) != 0) {
            throw new RuntimeException("El monto de pago (" + request.getMonto() + ") no coincide con el total de la venta (" + venta.getTotal() + ").");
        }

        // 3. Verificar si ya existe un pago para esta venta
        Optional<Pago> pagoExistente = pagoRepository.findByIdVenta(request.getIdVenta());
        if (pagoExistente.isPresent()) {
            throw new RuntimeException("Ya existe un pago registrado para la venta con ID: " + request.getIdVenta());
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
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<PagoResponseDTO> obtenerPagoPorVenta(Long idVenta) {
        return pagoRepository.findByIdVenta(idVenta).map(this::mapToResponse);
    }

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
