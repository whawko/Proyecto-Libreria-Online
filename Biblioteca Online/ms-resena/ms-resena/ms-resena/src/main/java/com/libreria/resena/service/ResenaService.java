package com.libreria.resena.service;

import com.libreria.resena.dto.*;
import com.libreria.resena.exception.ResenaNotFoundException;
import com.libreria.resena.feign.LibroFeignClient;
import com.libreria.resena.feign.UsuarioFeignClient;
import com.libreria.resena.model.Resena;
import com.libreria.resena.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final LibroFeignClient libroFeignClient;
    private final UsuarioFeignClient usuarioFeignClient;

    // ── CREAR ────────────────────────────────────────────────────
    public ResenaResponseDTO crear(ResenaRequestDTO dto) {

        // 1. Verificar que el libro existe (Feign lanza excepción si no)
        LibroDTO libro = libroFeignClient.obtenerLibroPorId(dto.getIdLibro());

        // 2. Verificar que el usuario existe
        UsuarioDTO usuario = usuarioFeignClient.obtenerUsuarioPorId(dto.getIdUsuario());

        // 3. Un usuario solo puede reseñar un libro una vez
        if (resenaRepository.existsByIdLibroAndIdUsuario(dto.getIdLibro(), dto.getIdUsuario())) {
            throw new IllegalArgumentException(
                "El usuario ya tiene una reseña para este libro"
            );
        }

        // 4. Guardar
        Resena resena = new Resena();
        resena.setIdLibro(dto.getIdLibro());
        resena.setIdUsuario(dto.getIdUsuario());
        resena.setTitulo(dto.getTitulo());
        resena.setComentario(dto.getComentario());
        resena.setCalificacion(dto.getCalificacion());
        Resena guardada = resenaRepository.save(resena);

        return toResponse(guardada, libro, usuario);
    }

    // ── LISTAR TODAS ─────────────────────────────────────────────
    public List<ResenaResponseDTO> listarTodas() {
        return resenaRepository.findAll().stream()
            .map(this::enriquecer)
            .collect(Collectors.toList());
    }

    // ── BUSCAR POR ID ────────────────────────────────────────────
    public ResenaResponseDTO buscarPorId(Long id) {
        Resena resena = resenaRepository.findById(id)
            .orElseThrow(() -> new ResenaNotFoundException(id));
        return enriquecer(resena);
    }

    // ── RESEÑAS POR LIBRO ─────────────────────────────────────────
    public List<ResenaResponseDTO> listarPorLibro(Long idLibro) {
        LibroDTO libro = libroFeignClient.obtenerLibroPorId(idLibro);
        return resenaRepository.findByIdLibroAndEstado(idLibro, "ACTIVO").stream()
            .map(r -> {
                UsuarioDTO usuario = usuarioFeignClient.obtenerUsuarioPorId(r.getIdUsuario());
                return toResponse(r, libro, usuario);
            })
            .collect(Collectors.toList());
    }

    // ── RESEÑAS POR USUARIO ───────────────────────────────────────
    public List<ResenaResponseDTO> listarPorUsuario(Long idUsuario) {
        UsuarioDTO usuario = usuarioFeignClient.obtenerUsuarioPorId(idUsuario);
        return resenaRepository.findByIdUsuarioAndEstado(idUsuario, "ACTIVO").stream()
            .map(r -> {
                LibroDTO libro = libroFeignClient.obtenerLibroPorId(r.getIdLibro());
                return toResponse(r, libro, usuario);
            })
            .collect(Collectors.toList());
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────
    public ResenaResponseDTO actualizar(Long id, ResenaRequestDTO dto) {
        Resena resena = resenaRepository.findById(id)
            .orElseThrow(() -> new ResenaNotFoundException(id));

        resena.setTitulo(dto.getTitulo());
        resena.setComentario(dto.getComentario());
        resena.setCalificacion(dto.getCalificacion());

        Resena actualizada = resenaRepository.save(resena);
        return enriquecer(actualizada);
    }

    // ── ELIMINAR (soft delete) ────────────────────────────────────
    public void eliminar(Long id) {
        Resena resena = resenaRepository.findById(id)
            .orElseThrow(() -> new ResenaNotFoundException(id));
        resena.setEstado("ELIMINADO");
        resenaRepository.save(resena);
    }

    // ── HELPERS ───────────────────────────────────────────────────
    private ResenaResponseDTO enriquecer(Resena resena) {
        LibroDTO libro = libroFeignClient.obtenerLibroPorId(resena.getIdLibro());
        UsuarioDTO usuario = usuarioFeignClient.obtenerUsuarioPorId(resena.getIdUsuario());
        return toResponse(resena, libro, usuario);
    }

    private ResenaResponseDTO toResponse(Resena resena, LibroDTO libro, UsuarioDTO usuario) {
        ResenaResponseDTO response = new ResenaResponseDTO();
        response.setIdResena(resena.getIdResena());
        response.setTitulo(resena.getTitulo());
        response.setComentario(resena.getComentario());
        response.setCalificacion(resena.getCalificacion());
        response.setFechaResena(resena.getFechaResena());
        response.setEstado(resena.getEstado());
        response.setLibro(libro);
        response.setUsuario(usuario);
        return response;
    }
}
