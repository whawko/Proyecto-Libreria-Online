package cl.syst3m64.envio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.envio.dto.VentaResponseDTO;

@FeignClient(name = "ms-venta")
public interface VentaFeignClient {

    @GetMapping("/api/ventas/{id}")
    VentaResponseDTO obtenerVentaPorId(@PathVariable("id") Long id);
}
