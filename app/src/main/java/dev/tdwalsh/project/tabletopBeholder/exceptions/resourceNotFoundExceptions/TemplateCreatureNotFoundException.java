package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class TemplateCreatureNotFoundException extends DBResourceNotFoundException{

    private static final long serialVersionUID = 2703872068858749333L;

    public TemplateCreatureNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TemplateCreatureNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateCreatureNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateCreatureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
