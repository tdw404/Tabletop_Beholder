package dev.tdwalsh.project.tabletopBeholder.exceptions.serializationExceptions;

public class TemplateSpellSerializationException extends RuntimeException{
    private static final long serialVersionUID = -3244924068422670823L;

    public TemplateSpellSerializationException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TemplateSpellSerializationException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateSpellSerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateSpellSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
