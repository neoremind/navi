package com.baidu.beidou.navi.exception;

/**
 * ClassName: ServiceNotFoundException <br/>
 * 
 * @author Zhang Xu
 */
public class ServiceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6138595796119511714L;

    public ServiceNotFoundException() {
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

}
