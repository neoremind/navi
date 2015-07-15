package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: DeserilizeNullException <br/>
 * Function: 解码后为空异常
 * 
 * @author Zhang Xu
 */
public class DeserilizeNullException extends RpcException {

    private static final long serialVersionUID = -6138595196129511714L;

    public DeserilizeNullException() {
    }

    public DeserilizeNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeserilizeNullException(String message) {
        super(message);
    }

    public DeserilizeNullException(Throwable cause) {
        super(cause);
    }

}
