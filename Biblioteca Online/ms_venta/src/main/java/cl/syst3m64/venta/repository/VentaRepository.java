package cl.syst3m64.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.syst3m64.venta.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

}
