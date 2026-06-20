package com.libreria.resena.feign;

import com.libreria.resena.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuario", url = "${ms.usuario.url}")
public interface UsuarioFeignClient {

    @GetMapping("/api/usuarios/{idUsuario}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("idUsuario") Long idUsuario);
}
