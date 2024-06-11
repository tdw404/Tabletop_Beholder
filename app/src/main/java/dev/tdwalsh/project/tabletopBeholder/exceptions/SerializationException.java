package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 1856361457854183093L;

    /**
     * Constructor.
     */
    public SerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public SerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public SerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
