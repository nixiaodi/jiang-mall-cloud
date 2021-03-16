package org.jiang.exception;

public class BooleanParseException extends RuntimeException {
    /**
     * Instantiates a new Boolean parse exception
     */
    public BooleanParseException() {
        super();
    }

    /**
     * Instantiates a new Boolean parse exception
     */
    public BooleanParseException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Boolean parse exception
     */
    public BooleanParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Boolean parse exception
     */
    public BooleanParseException(Throwable cause) {
        super(cause);
    }
}
