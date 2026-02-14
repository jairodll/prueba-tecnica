package gt.umg.gestionCobros.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class loteCobroResponsedto {
    private Integer total;
    private Integer procesados;
    private Integer fallidos;
    private Integer pendientes;
    private List<resultadoCobrodto> resultados;
}