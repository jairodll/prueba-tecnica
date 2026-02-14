package gt.umg.gestionCobros.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwt;
    private String correo;
    private String cui;
    private String nombre;
    private String rol;
}
