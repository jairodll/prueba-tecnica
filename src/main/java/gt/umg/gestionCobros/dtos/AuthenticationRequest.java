package gt.umg.gestionCobros.dtos;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String correo;
    private String password;
}
