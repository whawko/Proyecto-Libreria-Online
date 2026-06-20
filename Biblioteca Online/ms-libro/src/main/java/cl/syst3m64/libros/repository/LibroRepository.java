package cl.syst3m64.libros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.syst3m64.libros.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdCategoria(Long idCategoria);

    @Query("SELECT li FROM Libro li WHERE LOWER(li.titulo) LIKE LOWER(CONCAT('%',:titulo,'%'))")
    List<Libro> buscarPorTituloParecido(@Param("titulo") String titulo);
}
