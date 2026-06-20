package cl.syst3m64.libros.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-categoria", url = "${ms.categoria.url}")
public interface CategoriaFeignClient {

    @GetMapping("/api/categorias/{id}")
    Object obtenerCategoriaPorId(@PathVariable("id") Long id);
}
