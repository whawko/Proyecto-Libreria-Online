package cl.syst3m64.carrito.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.carrito.dto.CarritoRequestDTO;
import cl.syst3m64.carrito.dto.CarritoResponseDTO;
import cl.syst3m64.carrito.dto.LibroResponseDTO;
import cl.syst3m64.carrito.feign.LibroFeignClient;
import cl.syst3m64.carrito.feign.UsuarioFeignClient;
import cl.syst3m64.carrito.model.Carrito;
import cl.syst3m64.carrito.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioFeignClient usuarioFeignClient;
    private final LibroFeignClient libroFeignClient;

    @Transactional
    public CarritoResponseDTO agregarAlCarrito(CarritoRequestDTO request) {
        // 1. Validar que el usuario exista
        try {
            usuarioFeignClient.obtenerUsuarioPorId(request.getIdUsuario());
        } catch (Exception e) {
            throw new RuntimeException("El usuario con ID " + request.getIdUsuario() + " no existe o no pudo ser validado.");
        }

        // 2. Validar que el libro exista y obtener su precio
        LibroResponseDTO libro;
        try {
            libro = libroFeignClient.obtenerLibroPorId(request.getIdLibro());
            if (libro == null) {
                throw new RuntimeException("El libro con ID " + request.getIdLibro() + " no fue encontrado.");
            }
        } catch (Exception e) {
            throw new RuntimeException("El libro con ID " + request.getIdLibro() + " no existe o no pudo ser validado. Error: " + e.getMessage());
        }

        BigDecimal precioUnitario = BigDecimal.valueOf(libro.getPrecio());

        // 3. Verificar si el libro ya está en el carrito del usuario
        Optional<Carrito> carritoExistente = carritoRepository.findByIdUsuarioAndIdLibro(request.getIdUsuario(), request.getIdLibro());

        Carrito carrito;
        if (carritoExistente.isPresent()) {
            carrito = carritoExistente.get();
            carrito.setCantidad(carrito.getCantidad() + request.getCantidad());
            carrito.setSubtotal(precioUnitario.multiply(BigDecimal.valueOf(carrito.getCantidad())));
        } else {
            carrito = new Carrito();
            carrito.setIdUsuario(request.getIdUsuario());
            carrito.setIdLibro(request.getIdLibro());
            carrito.setCantidad(request.getCantidad());
            carrito.setPrecioUnitario(precioUnitario);
            carrito.setSubtotal(precioUnitario.multiply(BigDecimal.valueOf(request.getCantidad())));
        }

        Carrito saved = carritoRepository.save(carrito);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CarritoResponseDTO> obtenerCarritoPorUsuario(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CarritoResponseDTO actualizarCantidad(Long id, Integer cantidad) {
        if (cantidad < 1) {
            throw new RuntimeException("La cantidad mínima debe ser 1.");
        }

        Carrito carrito = carritoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado con ID: " + id));

        carrito.setCantidad(cantidad);
        carrito.setSubtotal(carrito.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));

        Carrito saved = carritoRepository.save(carrito);
        return mapToResponse(saved);
    }

    @Transactional
    public void eliminarDelCarrito(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new RuntimeException("Item del carrito no encontrado con ID: " + id);
        }
        carritoRepository.deleteById(id);
    }

    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        List<Carrito> items = carritoRepository.findByIdUsuario(idUsuario);
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito del usuario con ID " + idUsuario + " ya está vacío.");
        }
        carritoRepository.deleteAll(items);
    }

    private CarritoResponseDTO mapToResponse(Carrito carrito) {
        return new CarritoResponseDTO(
            carrito.getId(),
            carrito.getIdUsuario(),
            carrito.getIdLibro(),
            carrito.getCantidad(),
            carrito.getPrecioUnitario(),
            carrito.getSubtotal()
        );
    }
}
