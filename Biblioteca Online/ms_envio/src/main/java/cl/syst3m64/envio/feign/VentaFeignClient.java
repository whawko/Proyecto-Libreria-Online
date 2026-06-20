package cl.syst3m64.envio.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.envio.dto.VentaResponseDTO;

@FeignClient(name = "ms-venta-envio", url = "${ms.venta.url:http://localhost:8085}")
public interface VentaFeignClient {

    @GetMapping("/api/ventas/{id}")
    VentaResponseDTO obtenerVentaPorId(@PathVariable("id") Long id);
}
