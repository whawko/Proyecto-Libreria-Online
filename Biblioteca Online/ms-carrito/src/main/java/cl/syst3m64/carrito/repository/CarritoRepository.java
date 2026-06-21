package cl.syst3m64.carrito.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.syst3m64.carrito.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByIdUsuario(Long idUsuario);
    Optional<Carrito> findByIdUsuarioAndIdLibro(Long idUsuario, Long idLibro);
    void deleteByIdUsuario(Long idUsuario);
}
