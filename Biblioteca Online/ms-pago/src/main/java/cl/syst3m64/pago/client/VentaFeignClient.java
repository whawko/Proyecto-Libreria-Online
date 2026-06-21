package cl.syst3m64.pago.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import cl.syst3m64.pago.dto.VentaResponseDTO;

@FeignClient(name = "ms-venta")
public interface VentaFeignClient {

    @GetMapping("/api/ventas/{id}")
    VentaResponseDTO obtenerVentaPorId(@PathVariable("id") Long id);

    @PatchMapping("/api/ventas/{id}/estado")
    VentaResponseDTO actualizarEstadoVenta(@PathVariable("id") Long id, @RequestParam("idEstado") Long idEstado);
}
