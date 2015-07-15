package com.baidu.beidou.navi.exception;

/**
 * ClassName: InvalidRequestException <br/>
 * 
 * @author Zhang Xu
 */
public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = -6138595796119511714L;

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}
