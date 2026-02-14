package gt.umg.gestionCobros.repositories;

import gt.umg.gestionCobros.models.productos;
import gt.umg.gestionCobros.projections.productoProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface productoRepository extends CrudRepository<productos, Object>{

    @Query(value = "select p.descripcion as descripcion,\n" +
            "p.cantidad as cantidad,\n" +
            "p.precio as precio,\n" +
            "p.fecha_ingreso as fechaIngreso,\n" +
            "p.fecha_vencimiento as fechaVencimiento,\n" +
            "p.estado as estado\n" +
            "from dbo.productos p",
            nativeQuery = true)
    List<productoProjection> showProducto();
}
