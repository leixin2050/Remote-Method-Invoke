package com.lw.rmi.server.method;

/**
 * @author leiWei
 */
public class MethodNotFindException extends Exception {
    private static final long serialVersionUID = 6307463021065951367L;

    public MethodNotFindException() {
        super();
    }

    public MethodNotFindException(String message) {
        super(message);
    }

    public MethodNotFindException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFindException(Throwable cause) {
        super(cause);
    }

    protected MethodNotFindException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
