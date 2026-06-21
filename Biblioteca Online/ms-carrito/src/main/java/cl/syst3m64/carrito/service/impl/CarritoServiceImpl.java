package cl.syst3m64.carrito.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.carrito.dto.CarritoRequestDTO;
import cl.syst3m64.carrito.dto.CarritoResponseDTO;
import cl.syst3m64.carrito.dto.LibroResponseDTO;
import cl.syst3m64.carrito.client.LibroFeignClient;
import cl.syst3m64.carrito.client.UsuarioFeignClient;
import cl.syst3m64.carrito.exception.RecursoNoEncontradoException;
import cl.syst3m64.carrito.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.carrito.model.Carrito;
import cl.syst3m64.carrito.repository.CarritoRepository;
import cl.syst3m64.carrito.service.CarritoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioFeignClient usuarioFeignClient;
    private final LibroFeignClient libroFeignClient;

    @Override
    @Transactional
    public CarritoResponseDTO agregarAlCarrito(CarritoRequestDTO request) {
        // 1. Validar que el usuario exista
        try {
            usuarioFeignClient.obtenerUsuarioPorId(request.getIdUsuario());
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El usuario con ID " + request.getIdUsuario() + " no existe.");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el usuario con ID " + request.getIdUsuario() + ": " + e.getMessage());
        }

        // 2. Validar que el libro exista y obtener su precio
        LibroResponseDTO libro;
        try {
            libro = libroFeignClient.obtenerLibroPorId(request.getIdLibro());
            if (libro == null) {
                throw new RecursoNoEncontradoException("El libro con ID " + request.getIdLibro() + " no fue encontrado.");
            }
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El libro con ID " + request.getIdLibro() + " no existe.");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-libro: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el libro con ID " + request.getIdLibro() + ": " + e.getMessage());
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

    @Override
    @Transactional(readOnly = true)
    public List<CarritoResponseDTO> obtenerCarritoPorUsuario(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CarritoResponseDTO actualizarCantidad(Long id, Integer cantidad) {
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad mínima debe ser 1.");
        }

        Carrito carrito = carritoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Item del carrito no encontrado con ID: " + id));

        carrito.setCantidad(cantidad);
        carrito.setSubtotal(carrito.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));

        Carrito saved = carritoRepository.save(carrito);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void eliminarDelCarrito(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Item del carrito no encontrado con ID: " + id);
        }
        carritoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        List<Carrito> items = carritoRepository.findByIdUsuario(idUsuario);
        if (items.isEmpty()) {
            throw new RecursoNoEncontradoException("El carrito del usuario con ID " + idUsuario + " ya está vacío o no existe.");
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
