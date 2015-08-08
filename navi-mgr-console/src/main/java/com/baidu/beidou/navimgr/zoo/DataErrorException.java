package com.baidu.beidou.navimgr.zoo;

/**
 * Zookeeper client data exception
 * 
 * @author Zhang Xu
 */
public class DataErrorException extends RuntimeException {

    private static final long serialVersionUID = 3222573813262320183L;

    /**
     * Creates a new instance of DataErrorException.
     */
    public DataErrorException() {
    }

    /**
     * Creates a new instance of DataErrorException.
     * 
     * @param message
     *            错误信息
     * @param cause
     *            原因
     */
    public DataErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of DataErrorException.
     * 
     * @param message
     *            错误信息
     */
    public DataErrorException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of DataErrorException.
     * 
     * @param cause
     *            原因
     */
    public DataErrorException(Throwable cause) {
        super(cause);
    }

}
