package gt.umg.gestionCobros.exceptions;

import java.util.List;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

public class MSRinconException extends GeneralResponseException {
    private final ErrorEnum error;
    private final transient List<ErrorDetail> errores;
    private final transient Object[] paramError;
    private final String messageOverwrite;

    public MSRinconException(ErrorEnum pError) {
        super(pError.toString());
        this.error = pError;
        this.errores = null;
        this.paramError = null;
        this.messageOverwrite = null;
    }

    public MSRinconException(ErrorEnum pError, Object[] pParamError) {
        super(pError.toString());
        this.error = pError;
        this.errores = null;
        this.paramError = pParamError;
        this.messageOverwrite = null;
    }

    public MSRinconException(ErrorEnum pError, List<ErrorDetail> pErrores) {
        super(pError.toString());
        this.error = pError;
        this.errores = pErrores;
        this.paramError = null;
        this.messageOverwrite = null;
    }

    public MSRinconException(ErrorEnum pError, Throwable pCause) {
        //super(pError.toString(), pCause);
        this.error = pError;
        this.errores = null;
        this.paramError = null;
        this.messageOverwrite = null;
    }

    public MSRinconException(String mensaje) {
        super(mensaje);
        this.error = null;
        this.errores = null;
        this.paramError = null;
        this.messageOverwrite = null;
    }

    public MSRinconException(ErrorEnum pError, String message) {
        super(pError.toString());
        this.error = pError;
        this.errores = null;
        this.paramError = null;
        this.messageOverwrite = message;
    }

    public ErrorEnum getError() {
        return error;
    }

    public List<ErrorDetail> getErrores() {
        return errores;
    }

    public Object[] getParamError() {
        return paramError;
    }

    public String getMessageOverwrite() {
        return messageOverwrite;
    }
}
