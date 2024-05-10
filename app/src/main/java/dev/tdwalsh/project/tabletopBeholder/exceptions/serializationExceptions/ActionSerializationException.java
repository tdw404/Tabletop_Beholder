package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class ActionSerializationException extends RuntimeException {

    private static final long serialVersionUID = 4854057893671725354L;

    public ActionSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public ActionSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public ActionSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public ActionSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
