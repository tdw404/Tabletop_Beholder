package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class CurlException extends RuntimeException{

    private static final long serialVersionUID = 6960459999917103220L;

    public CurlException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public CurlException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public CurlException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CurlException(String message, Throwable cause) {
        super(message, cause);
    }
}
