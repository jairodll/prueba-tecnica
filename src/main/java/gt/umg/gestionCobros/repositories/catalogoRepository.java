package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.catalogos;
import gt.umg.gestionCobros.projections.catalogoProjection;
import gt.umg.gestionCobros.projections.compraProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface catalogoRepository extends CrudRepository<catalogos, Object> {

    @Query(value = "SELECT c.id_catalogo as idCatalogo, " +
            "c.nombre_catalogo as nombreCatalogo, " +
            "c.descripcion_catalogo as descripcionCatalogo, " +
            "c.fecha_creacion as fechaCreacion, " +
            "c.estado as estado, " +
            "c.fecha_modificacion as fechaModificacion, " +
            "tc.nombre AS nombre " +
            "FROM dbo.catalogos c " +
            "JOIN dbo.tipo_catalogos tc ON c.id_tipo_catalogo = tc.id_tipo_catalogo " +
            "WHERE tc.nombre = :tipoCatalogo", nativeQuery = true)
    List<catalogoProjection> findCatalogoByTipo(@Param("tipoCatalogo") String tipoCatalogo);

    @Query(value = "select v.total total,\n" +
            "p.descripcion descripcion, \n" +
            "v.fecha_venta fechaVenta,\n" +
            "v.id_venta idVenta,\n" +
            "p.id_producto idProducto,\n" +
            "v.cantidad cantidad\n" +
            "from dbo.ventas v\n" +
            "inner join dbo.productos p on p.id_producto = v.id_producto \n" +
            "where v.id_usuario = :idUsuario", nativeQuery = true)
    List<compraProjection> findCompraById(@Param("idUsuario") Integer idUsuario);

}
