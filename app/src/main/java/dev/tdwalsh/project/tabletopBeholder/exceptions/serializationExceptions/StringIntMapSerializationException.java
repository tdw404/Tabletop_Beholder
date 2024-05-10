package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class StringIntMapSerializationException extends RuntimeException{

    private static final long serialVersionUID = 1856361457854183093L;

    public StringIntMapSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public StringIntMapSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public StringIntMapSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public StringIntMapSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
