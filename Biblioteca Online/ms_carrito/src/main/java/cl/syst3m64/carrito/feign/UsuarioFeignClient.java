package cl.syst3m64.carrito.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuario-carrito", url = "${ms.usuario.url:http://localhost:8084}")
public interface UsuarioFeignClient {

    @GetMapping("/api/usuarios/{id}")
    Object obtenerUsuarioPorId(@PathVariable("id") Long id);
}
