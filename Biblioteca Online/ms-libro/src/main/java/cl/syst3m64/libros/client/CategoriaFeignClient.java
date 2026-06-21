package cl.syst3m64.libros.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.libros.dto.CategoriaDTO;

@FeignClient(name = "ms-categoria")
public interface CategoriaFeignClient {

    @GetMapping("/api/categorias/{id}")
    CategoriaDTO obtenerCategoriaPorId(@PathVariable("id") Long id);
}

