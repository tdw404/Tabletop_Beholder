package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class EncounterNotFoundException extends DBResourceNotFoundException {

    private static final long serialVersionUID = -3147431880890824379L;

    /**
     * Constructor.
     */
    public EncounterNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EncounterNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EncounterNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EncounterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
