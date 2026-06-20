package cl.syst3m64.pago.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.syst3m64.pago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByIdVenta(Long idVenta);
    Optional<Pago> findByTransaccionId(String transaccionId);
}
