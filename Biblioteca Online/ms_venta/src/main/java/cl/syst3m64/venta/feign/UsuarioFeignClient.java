package cl.syst3m64.venta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuario-venta", url = "${ms.usuario.url}")
public interface UsuarioFeignClient {

    @GetMapping("/api/usuarios/{id}")
    Object obtenerUsuarioPorId(@PathVariable("id") Long id);
}
