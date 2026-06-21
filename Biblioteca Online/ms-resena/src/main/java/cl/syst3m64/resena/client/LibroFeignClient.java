package cl.syst3m64.resena.client;

import cl.syst3m64.resena.dto.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-libro")
public interface LibroFeignClient {

    @GetMapping("/api/libros/{idLibro}")
    LibroDTO obtenerLibroPorId(@PathVariable("idLibro") Long idLibro);
}

