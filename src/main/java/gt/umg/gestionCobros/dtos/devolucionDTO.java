package gt.umg.gestionCobros.dtos;

import lombok.Data;

@Data
public class devolucionDTO {
private String cantidadDevuelta;
private String fechaDevolucion;
private String idProducto;
private String idVenta;
private String motivoDevolucion;
}
