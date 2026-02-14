package gt.umg.gestionCobros.projections;

public interface productoProjection {
    String getEstado();
    String getCantidad();

    String getPrecio();

    String getFechaIngreso();

    String getDescripcion();

    String getFechaVencimiento();

    String getIdProducto();
}
