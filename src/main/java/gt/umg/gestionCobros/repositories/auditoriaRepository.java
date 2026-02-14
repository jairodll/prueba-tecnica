package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface auditoriaRepository extends JpaRepository<auditoria, Integer> {

    List<auditoria> findByEvento(String evento);

    List<auditoria> findByEntidadAndIdEntidad(String entidad, Integer idEntidad);

    List<auditoria> findTop50ByOrderByFechaDesc();
}