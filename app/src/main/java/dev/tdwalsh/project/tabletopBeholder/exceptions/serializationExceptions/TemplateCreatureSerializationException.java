package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class TemplateCreatureSerializationException extends RuntimeException{
    private static final long serialVersionUID = 8268664989780028532L;

    public TemplateCreatureSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TemplateCreatureSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateCreatureSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateCreatureSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
