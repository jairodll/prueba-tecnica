package gt.umg.gestionCobros.exceptions;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

public class OperationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OperationException(Exception e) {
        super(e);
    }

    public OperationException(final String message) {
        super(message);
    }
}
