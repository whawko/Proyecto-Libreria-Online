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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito", description = "Controlador para gestionar el carrito de compras de los usuarios")
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping
    @Operation(summary = "Agregar un libro al carrito", description = "Agrega un libro al carrito de un usuario. Si ya existe, incrementa la cantidad.")
    @ApiResponse(responseCode = "201", description = "Libro agregado al carrito exitosamente")
    public ResponseEntity<CarritoResponseDTO> agregarAlCarrito(@Valid @RequestBody CarritoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarAlCarrito(request));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener el carrito de un usuario", description = "Recupera la lista de items en el carrito para un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Carrito recuperado exitosamente")
    @ApiResponse(responseCode = "204", description = "El carrito está vacío", content = @Content)
    public ResponseEntity<List<CarritoResponseDTO>> obtenerCarritoPorUsuario(@PathVariable Long idUsuario) {
        List<CarritoResponseDTO> items = carritoService.obtenerCarritoPorUsuario(idUsuario);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar la cantidad de un item", description = "Actualiza la cantidad de un libro específico en el carrito.")
    @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un item del carrito", description = "Elimina un libro específico del carrito de compras.")
    @ApiResponse(responseCode = "200", description = "Item eliminado del carrito exitosamente")
    public ResponseEntity<String> eliminarDelCarrito(@PathVariable Long id) {
        carritoService.eliminarDelCarrito(id);
        return ResponseEntity.ok("Item eliminado del carrito exitosamente.");
    }

    @DeleteMapping("/usuario/{idUsuario}")
    @Operation(summary = "Vaciar el carrito de un usuario", description = "Elimina todos los items del carrito de un usuario específico.")
    @ApiResponse(responseCode = "200", description = "Carrito vaciado exitosamente")
    public ResponseEntity<String> vaciarCarrito(@PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok("Carrito vaciado exitosamente.");
    }
}
