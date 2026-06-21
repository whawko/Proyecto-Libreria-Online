package cl.syst3m64.envio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.envio.dto.DireccionResponseDTO;

@FeignClient(name = "ms-direccion")
public interface DireccionFeignClient {

    @GetMapping("/api/direcciones/{id}")
    DireccionResponseDTO obtenerDireccionPorId(@PathVariable("id") Long id);
}
