package com.libreria.resena.feign;

import com.libreria.resena.dto.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// url viene de application.properties → ms.libro.url
@FeignClient(name = "ms-libro", url = "${ms.libro.url}")
public interface LibroFeignClient {

    @GetMapping("/api/libros/{idLibro}")
    LibroDTO obtenerLibroPorId(@PathVariable("idLibro") Long idLibro);
}
