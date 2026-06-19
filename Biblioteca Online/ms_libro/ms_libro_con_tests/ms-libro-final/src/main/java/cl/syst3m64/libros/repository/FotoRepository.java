package cl.syst3m64.libros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.syst3m64.libros.model.Foto;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByLibroId(Long idLibro);
}
