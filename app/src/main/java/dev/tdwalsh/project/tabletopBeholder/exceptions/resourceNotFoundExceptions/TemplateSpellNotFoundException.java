package dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions;

public class TemplateSpellNotFoundException extends DBResourceNotFoundException{
    private static final long serialVersionUID = 7708501017167524741L;

    public TemplateSpellNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TemplateSpellNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateSpellNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateSpellNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
