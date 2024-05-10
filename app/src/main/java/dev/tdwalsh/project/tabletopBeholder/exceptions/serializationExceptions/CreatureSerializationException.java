package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class CreatureSerializationException extends RuntimeException {

    private static final long serialVersionUID = 2594731283890889689L;

    public CreatureSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public CreatureSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public CreatureSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CreatureSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
