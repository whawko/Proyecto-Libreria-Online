package cl.syst3m64.usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.usuario.dto.EstadoDTO;

@FeignClient(name = "ms-estado")
public interface EstadoFeignClient {

    @GetMapping("/api/estados/{id}")
    EstadoDTO obtenerEstadoPorId(@PathVariable("id") Long id);
}
