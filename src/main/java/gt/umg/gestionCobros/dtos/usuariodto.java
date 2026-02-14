package gt.umg.gestionCobros.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class usuariodto {
    private Integer idUsuario;
    private String cui;
    private String nombre;
    private String correo;
    private String estado;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private String password;
    private Long rol;
}
