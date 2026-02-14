package gt.umg.gestionCobros.exceptions;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

public class ErrorDetail {
    private final int codigo;
    private final String campo;
    private final String mensaje;

    public ErrorDetail(int codigo, String campo, String mensaje) {
        this.codigo = codigo;
        this.campo = campo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
