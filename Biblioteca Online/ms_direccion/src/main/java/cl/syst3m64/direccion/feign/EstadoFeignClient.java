package cl.syst3m64.direccion.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-estado-direccion", url = "${ms.estado.url}")
public interface EstadoFeignClient {

    @GetMapping("/api/estados/{id}")
    Object obtenerEstadoPorId(@PathVariable("id") Long id);
}
