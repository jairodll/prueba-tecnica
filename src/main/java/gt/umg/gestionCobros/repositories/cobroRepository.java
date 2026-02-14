package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.cobros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface cobroRepository extends JpaRepository<cobros, Integer> {

    // Buscar cobros por usuario
    List<cobros> findByIdUsuario(Integer idUsuario);

    // Buscar cobros por estado
    List<cobros> findByEstado(String estado);

    // Buscar cobros por usuario y estado
    List<cobros> findByIdUsuarioAndEstado(Integer idUsuario, String estado);

    // Buscar cobros por usuario con filtros opcionales
    @Query("SELECT c FROM cobros c WHERE c.idUsuario = :idUsuario " +
            "AND (:estado IS NULL OR c.estado = :estado) " +
            "AND (:desde IS NULL OR c.fechaCreacion >= :desde) " +
            "AND (:hasta IS NULL OR c.fechaCreacion <= :hasta) " +
            "ORDER BY c.fechaCreacion DESC")
    List<cobros> findByUsuarioWithFilters(
            @Param("idUsuario") Integer idUsuario,
            @Param("estado") String estado,
            @Param("desde") Date desde,
            @Param("hasta") Date hasta
    );
}