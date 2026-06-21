package cl.syst3m64.direccion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.syst3m64.direccion.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}
