package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: ServerErrorException <br/>
 * 
 * @author Zhang Xu
 */
public class ServerErrorException extends RpcException {

    private static final long serialVersionUID = 9029113876648829300L;

    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
