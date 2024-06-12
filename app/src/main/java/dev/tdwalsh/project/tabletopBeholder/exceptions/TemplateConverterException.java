package dev.tdwalsh.project.tabletopBeholder.exceptions;

public class TemplateConverterException extends RuntimeException {


    private static final long serialVersionUID = -4127233423372913140L;

    /**
     * Constructor.
     */
    public TemplateConverterException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TemplateConverterException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateConverterException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TemplateConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
