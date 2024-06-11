package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class SpellNotFoundException extends DBResourceNotFoundException {

    private static final long serialVersionUID = -6438113079975039107L;

    /**
     * Constructor.
     */
    public SpellNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public SpellNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public SpellNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public SpellNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
