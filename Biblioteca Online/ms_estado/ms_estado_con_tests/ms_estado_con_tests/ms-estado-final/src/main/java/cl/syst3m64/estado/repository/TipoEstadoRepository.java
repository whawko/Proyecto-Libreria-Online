package cl.syst3m64.estado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.syst3m64.estado.model.TipoEstado;

@Repository
public interface TipoEstadoRepository extends JpaRepository<TipoEstado, Long> {

}
