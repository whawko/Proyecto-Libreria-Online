package cl.syst3m64.venta.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import cl.syst3m64.venta.dto.CarritoItemDTO;

@FeignClient(name = "ms-carrito")
public interface CarritoFeignClient {

    @GetMapping("/api/carrito/usuario/{idUsuario}")
    List<CarritoItemDTO> obtenerCarritoPorUsuario(@PathVariable("idUsuario") Long idUsuario);

    @DeleteMapping("/api/carrito/usuario/{idUsuario}")
    void vaciarCarrito(@PathVariable("idUsuario") Long idUsuario);
}
