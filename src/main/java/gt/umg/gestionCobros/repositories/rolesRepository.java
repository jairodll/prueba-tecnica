package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.projections.rolesProjection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface rolesRepository  extends CrudRepository<usuarios, Object> {
    @Query(value = "select r.id_rol rol, \n" +
            "r.nombre nombre  \n" +
            "from dbo.roles r ",
            nativeQuery = true)
    List<rolesProjection> showRoles();

    @Modifying
    @Query(value = "INSERT INTO dbo.usuario_roles (id_usuario, id_rol) VALUES (:idUsuario, :idRol)", nativeQuery = true)
    void assignRoleToUser(@Param("idUsuario") Long idUsuario, @Param("idRol") Long idRol);

}

