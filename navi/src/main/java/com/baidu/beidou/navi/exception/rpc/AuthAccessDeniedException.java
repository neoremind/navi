package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: AuthAccessDeniedException <br/>
 * 
 * @author Zhang Xu
 */
public class AuthAccessDeniedException extends RpcException {

    private static final long serialVersionUID = -1760095868220273812L;

    public AuthAccessDeniedException() {
        super();
    }

    public AuthAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthAccessDeniedException(String message) {
        super(message);
    }

    public AuthAccessDeniedException(Throwable cause) {
        super(cause);
    }

}
