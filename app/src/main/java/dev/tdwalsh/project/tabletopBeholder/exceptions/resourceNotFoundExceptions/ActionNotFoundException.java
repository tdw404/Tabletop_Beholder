package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class ActionNotFoundException extends DBResourceNotFoundException{

    private static final long serialVersionUID = -1493451373893866881L;

    public ActionNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public ActionNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public ActionNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public ActionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
