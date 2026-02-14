package gt.umg.gestionCobros.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class cobrodto {

    private Integer idUsuario;
    private BigDecimal monto;
    private String moneda;
    private String descripcion;
    private String referenciaExterna;
}