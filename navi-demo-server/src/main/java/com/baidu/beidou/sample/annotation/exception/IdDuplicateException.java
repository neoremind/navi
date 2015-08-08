package com.baidu.beidou.sample.annotation.exception;

/**
 * 
 * ClassName: IdDuplicateException <br/>
 * Function: ID重复异常
 *
 * @author zhangxu04
 */
public class IdDuplicateException extends RuntimeException {

    private static final long serialVersionUID = -6138595796119511714L;

    public IdDuplicateException() {
    }

    public IdDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdDuplicateException(String message) {
        super(message);
    }

    public IdDuplicateException(Throwable cause) {
        super(cause);
    }

}
