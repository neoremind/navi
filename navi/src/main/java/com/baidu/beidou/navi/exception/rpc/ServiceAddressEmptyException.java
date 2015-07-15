package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: ServiceAddressEmptyException <br/>
 * 
 * @author Zhang Xu
 */
public class ServiceAddressEmptyException extends RpcException {

    private static final long serialVersionUID = -2984282469636914907L;

    public ServiceAddressEmptyException() {
    }

    public ServiceAddressEmptyException(String message) {
        super(message);
    }

    public ServiceAddressEmptyException(Throwable cause) {
        super(cause);
    }

    public ServiceAddressEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

}
