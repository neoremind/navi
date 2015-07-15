package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: RpcTimeoutException <br/>
 * 
 * @author Zhang Xu
 */
public class RpcTimeoutException extends RpcException {

    private static final long serialVersionUID = 9029113876648829300L;

    public RpcTimeoutException() {
    }

    public RpcTimeoutException(String message) {
        super(message);
    }

    public RpcTimeoutException(Throwable cause) {
        super(cause);
    }

    public RpcTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
