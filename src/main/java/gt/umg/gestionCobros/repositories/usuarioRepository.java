package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.projections.usuariosProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface usuarioRepository extends CrudRepository<usuarios, Object> {
    Optional<usuarios> findByCorreo(String correo);

    @Query(value = "select r.nombre from usuario_roles ur \n" +
            "inner join dbo.roles r on r.id_rol = ur.id_rol \n" +
            "where ur.id_usuario = :iduser", nativeQuery = true)
    String findRolById(@Param("iduser") Integer iduser);

    @Query(value = "select \n" +
            "u.cui cui, \n" +
            "u.nombre nombre, \n" +
            "u.correo correo, \n" +
            "u.fecha_creacion fechaCreacion,\n" +
            "r.nombre Rol\n" +
            "from dbo.usuarios u\n" +
            "inner join dbo.usuario_roles ur on u.id_usuario = ur.id_usuario \n" +
            "inner join dbo.roles r on r.id_rol = ur.id_rol ",
            nativeQuery = true)
    List<usuariosProjection> showUsers();
}
