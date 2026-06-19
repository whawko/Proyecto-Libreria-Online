package cl.syst3m64.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.syst3m64.usuario.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

}
