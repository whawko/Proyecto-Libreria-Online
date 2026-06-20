package com.libreria.resena.repository;

import com.libreria.resena.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    // Todas las reseñas de un libro
    List<Resena> findByIdLibroAndEstado(Long idLibro, String estado);

    // Todas las reseñas de un usuario
    List<Resena> findByIdUsuarioAndEstado(Long idUsuario, String estado);

    // Verificar si un usuario ya reseñó un libro
    boolean existsByIdLibroAndIdUsuario(Long idLibro, Long idUsuario);
}
