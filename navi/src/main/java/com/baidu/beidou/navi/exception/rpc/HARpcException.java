package com.baidu.beidou.navi.exception.rpc;

/**
 * 
 * ClassName: HARpcException <br/>
 * Function: 启用ha策略的调用异常
 *
 * @author Zhang Xu
 */
public class HARpcException extends RpcException {

    private static final long serialVersionUID = 3599653969669270363L;

    public HARpcException() {
        super();
    }

    public HARpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public HARpcException(String message) {
        super(message);
    }

    public HARpcException(Throwable cause) {
        super(cause);
    }

}
