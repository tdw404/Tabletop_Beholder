package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class MissingResourceException extends RuntimeException{

    private static final long serialVersionUID = -8247334877320501035L;

    public MissingResourceException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public MissingResourceException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public MissingResourceException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public MissingResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
