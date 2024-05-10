package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class SpellSerializationException extends RuntimeException{

    private static final long serialVersionUID = 2742072458537789813L;

    public SpellSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public SpellSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public SpellSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public SpellSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
