package cl.syst3m64.carrito.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cl.syst3m64.carrito.dto.CarritoRequestDTO;
import cl.syst3m64.carrito.dto.CarritoResponseDTO;
import cl.syst3m64.carrito.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping
    public ResponseEntity<CarritoResponseDTO> agregarAlCarrito(@Valid @RequestBody CarritoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarAlCarrito(request));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CarritoResponseDTO>> obtenerCarritoPorUsuario(@PathVariable Long idUsuario) {
        List<CarritoResponseDTO> items = carritoService.obtenerCarritoPorUsuario(idUsuario);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable Long id) {
        carritoService.eliminarDelCarrito(id);
        return ResponseEntity.ok("Item eliminado del carrito exitosamente.");
    }

    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<String> vaciarCarrito(@PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok("Carrito vaciado exitosamente.");
    }
}
