package cl.syst3m64.carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.carrito.dto.LibroResponseDTO;

@FeignClient(name = "ms-libro")
public interface LibroFeignClient {

    @GetMapping("/api/libros/{id}")
    LibroResponseDTO obtenerLibroPorId(@PathVariable("id") Long id);
}
