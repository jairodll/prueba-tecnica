package gt.umg.gestionCobros.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class resultadoCobrodto {
    private Integer idCobro;
    private String estadoAnterior;
    private String estadoNuevo;
    private String mensaje;
}