package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class EncounterSerializationException extends RuntimeException{

    private static final long serialVersionUID = 3931237969516209780L;

    public EncounterSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EncounterSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EncounterSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EncounterSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
