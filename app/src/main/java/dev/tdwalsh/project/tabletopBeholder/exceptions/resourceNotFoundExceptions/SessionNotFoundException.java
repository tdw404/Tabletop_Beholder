package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class SessionNotFoundException extends DBResourceNotFoundException{


    private static final long serialVersionUID = 1563017179320693235L;

    public SessionNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public SessionNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public SessionNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
