package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class CreatureNotFoundException extends DBResourceNotFoundException {

    private static final long serialVersionUID = 7786692599125919823L;

    /**
     * Constructor.
     */
    public CreatureNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public CreatureNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public CreatureNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CreatureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
