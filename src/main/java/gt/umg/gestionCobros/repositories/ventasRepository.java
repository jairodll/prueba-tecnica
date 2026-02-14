package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.ventas;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.sql.Timestamp;

public interface ventasRepository extends CrudRepository <ventas,Object> {

    @Modifying
    @Query(value = "INSERT INTO ventas (fecha_venta, id_usuario, cantidad, precio_unitario, total, id_producto, id_catalogo) " +
            "VALUES (:fechaVenta, :idUsuario, :cantidad, :precioUnitario, :total, :idProducto, :idCatalogo)", nativeQuery = true)
    void registrarVenta(@Param("fechaVenta") Timestamp fechaVenta,
                        @Param("idUsuario") Integer idUsuario,
                        @Param("cantidad") Integer cantidad,
                        @Param("precioUnitario") Double precioUnitario,
                        @Param("total") Double total,
                        @Param("idProducto") Integer idProducto,
                        @Param("idCatalogo") Integer idCatalogo);


}
