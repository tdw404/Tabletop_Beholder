package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class DuplicateResourceException extends RuntimeException{

    private static final long serialVersionUID = 9111179180093207396L;

    public DuplicateResourceException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
