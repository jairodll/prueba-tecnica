package gt.umg.gestionCobros.dtos;

import lombok.Data;
import java.util.List;

@Data
public class reciboDto {
    private String noRecibo;
    private FacturacionDto datosFacturacion;
    private MetodoPagoDto metodoPago;
    private List<ProductoDto> productos;



}
