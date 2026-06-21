package cl.syst3m64.envio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.syst3m64.envio.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Optional<Envio> findByIdVenta(Long idVenta);
    Optional<Envio> findByNumeroSeguimiento(String numeroSeguimiento);
}
