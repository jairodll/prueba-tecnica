package gt.umg.gestionCobros.exceptions;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

public class GenericException extends RuntimeException{
    private Integer statusCode;
    private String moreInfo;

    public GenericException(Exception e) {
        super(e);
    }

    public GenericException(final String message) {
        super(message);
    }

    public GenericException(final String message, final String moreInfo) {
        super(message);
        this.moreInfo = moreInfo;
    }

    public GenericException(final String message, final String moreInfo, int statusCode) {
        super(message);
        this.moreInfo = moreInfo;
        this.statusCode = statusCode;
    }

    public GenericException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GenericException(final String message, final Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public GenericException(final String message, int statusCode) {
        this(message);
        this.statusCode = statusCode;
    }

    public boolean hasStatusCode() {
        return this.statusCode != null;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMoreInfo() {
        return moreInfo;
    }
}
