package gt.umg.gestionCobros.dtos;

import lombok.Data;

@Data
public class infoReciboDto {
    private String noRecibo;
    private String correo;
    private String cui;
    private String nombreCliente;
    private String nombreCatalogo;
    private String descripcion;
    private int cantidad;
    private Double precio;
    private Double subtotal;
}
