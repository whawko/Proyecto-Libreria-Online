package cl.syst3m64.resena.client;

import cl.syst3m64.resena.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuario")
public interface UsuarioFeignClient {

    @GetMapping("/api/usuarios/{idUsuario}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("idUsuario") Long idUsuario);
}

