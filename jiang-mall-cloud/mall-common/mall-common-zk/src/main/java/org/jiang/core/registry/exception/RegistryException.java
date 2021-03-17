package org.jiang.core.registry.exception;

/**
 * 注册中心异常
 * @author 蒋小胖
 */
public class RegistryException extends RuntimeException{
    private static final long serialVersionUID = -6417179023552012152L;

    /**
     * Instantiates a new Registry exception.
     * @param errorMessage the error message
     * @param args         the args
     */
    public RegistryException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    /**
     * Instantiates a new Registry exception.
     * @param cause the cause
     */
    public RegistryException(final Exception cause) {
        super(cause);
    }
}
