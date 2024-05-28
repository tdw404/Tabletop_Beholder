package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class MalformedInputException extends RuntimeException{

    private static final long serialVersionUID = 8737421218023502169L;

    public MalformedInputException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public MalformedInputException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public MalformedInputException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public MalformedInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
