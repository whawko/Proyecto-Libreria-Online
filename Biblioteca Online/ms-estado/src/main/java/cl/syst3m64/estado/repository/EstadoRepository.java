package cl.syst3m64.estado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.syst3m64.estado.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
   

}
