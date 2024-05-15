package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class CommunicationException extends RuntimeException{

    private static final long serialVersionUID = 6960459999917103220L;

    public CommunicationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public CommunicationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public CommunicationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
