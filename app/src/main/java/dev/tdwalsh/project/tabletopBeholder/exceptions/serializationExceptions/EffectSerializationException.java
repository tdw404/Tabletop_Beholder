package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class EffectSerializationException extends RuntimeException{
    private static final long serialVersionUID = 2617468249332261754L;

    public EffectSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public EffectSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public EffectSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public EffectSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
