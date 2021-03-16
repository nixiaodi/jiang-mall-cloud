package org.jiang.exception;

public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = 6480772904575978373L;

    /**
     * Instantiates a new Config exception.
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Config exception.
     */
    public ConfigException() {

    }
}
