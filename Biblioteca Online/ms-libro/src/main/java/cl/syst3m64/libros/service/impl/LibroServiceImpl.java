package cl.syst3m64.libros.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.syst3m64.libros.dto.LibroRequestDTO;
import cl.syst3m64.libros.dto.LibroResponseDTO;
import cl.syst3m64.libros.client.CategoriaFeignClient;
import cl.syst3m64.libros.client.EstadoFeignClient;
import cl.syst3m64.libros.exception.RecursoNoEncontradoException;
import cl.syst3m64.libros.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.LibroRepository;
import cl.syst3m64.libros.service.LibroService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final CategoriaFeignClient categoriaFeignClient;
    private final EstadoFeignClient estadoFeignClient;

    private LibroResponseDTO mapToDTO(Libro li) {
        return new LibroResponseDTO(
            li.getId(), li.getTitulo(), li.getDescripcion(),
            li.getAutor(), li.getIsbn(), li.getPrecio(),
            li.getAnnio(), li.getEditorial(),
            li.getIdCategoria(), li.getIdEstado()
        );
    }

    @Override
    public void validarCategoria(Long categoriaId) {
        try {
            categoriaFeignClient.obtenerCategoriaPorId(categoriaId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("La categoría con ID: " + categoriaId + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_categoria: " + e.getMessage());
        }
    }

    @Override
    public void validarEstado(Long estadoId) {
        try {
            estadoFeignClient.obtenerEstadoPorId(estadoId);
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("El estado con ID: " + estadoId + " no existe");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    @Override
    public List<LibroResponseDTO> obtenerLibros() {
        return libroRepository.findAll().stream()
            .map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<LibroResponseDTO> obtenerPorId(Long id) {
        return libroRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public LibroResponseDTO guardarLibro(LibroRequestDTO libro) {
        validarCategoria(libro.getIdCategoria());
        validarEstado(libro.getIdEstado());
        Libro lib = new Libro(null, libro.getTitulo(), libro.getDescripcion(), libro.getAutor(),
            libro.getIsbn(), libro.getPrecio(), libro.getAnnio(), libro.getEditorial(),
            libro.getIdCategoria(), libro.getIdEstado());
        return mapToDTO(libroRepository.save(lib));
    }

    @Override
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libro) {
        validarCategoria(libro.getIdCategoria());
        validarEstado(libro.getIdEstado());
        return libroRepository.findById(id).map(existing -> {
            existing.setTitulo(libro.getTitulo());
            existing.setDescripcion(libro.getDescripcion());
            existing.setAutor(libro.getAutor());
            existing.setIsbn(libro.getIsbn());
            existing.setPrecio(libro.getPrecio());
            existing.setAnnio(libro.getAnnio());
            existing.setEditorial(libro.getEditorial());
            existing.setIdCategoria(libro.getIdCategoria());
            existing.setIdEstado(libro.getIdEstado());
            return mapToDTO(libroRepository.save(existing));
        }).orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado con ID: " + id));
    }

    @Override
    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }
}
