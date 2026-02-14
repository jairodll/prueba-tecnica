package gt.umg.gestionCobros.dtos;

import lombok.Data;

@Data
public class ProductoDto {
    private String descripcion;
    private int cantidad;
    private Double subtotal;
    private int idProducto;
    private Double precio;
}
