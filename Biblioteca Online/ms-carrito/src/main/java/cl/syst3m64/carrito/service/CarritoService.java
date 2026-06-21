package cl.syst3m64.carrito.service;

import java.util.List;
import cl.syst3m64.carrito.dto.CarritoRequestDTO;
import cl.syst3m64.carrito.dto.CarritoResponseDTO;

public interface CarritoService {
    CarritoResponseDTO agregarAlCarrito(CarritoRequestDTO request);
    List<CarritoResponseDTO> obtenerCarritoPorUsuario(Long idUsuario);
    CarritoResponseDTO actualizarCantidad(Long id, Integer cantidad);
    void eliminarDelCarrito(Long id);
    void vaciarCarrito(Long idUsuario);
}

