package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: InvalidParamException <br/>
 * 
 * @author Zhang Xu
 */
public class InvalidParamException extends RpcException {

    private static final long serialVersionUID = -2871706631475075323L;

    public InvalidParamException() {
        super();
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(Throwable cause) {
        super(cause);
    }

}
