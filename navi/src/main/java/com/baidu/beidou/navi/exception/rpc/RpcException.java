package com.baidu.beidou.navi.exception.rpc;

/**
 * Base rpc exception <br>
 * All exception should inherit this super exception
 * 
 * @author Zhang Xu
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 3599653969669270363L;

    public RpcException() {
        super();
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

}
