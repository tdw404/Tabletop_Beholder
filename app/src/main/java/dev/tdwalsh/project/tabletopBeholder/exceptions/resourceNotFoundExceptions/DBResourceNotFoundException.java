package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class DBResourceNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -6919308022947662551L;

    public DBResourceNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DBResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DBResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DBResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
