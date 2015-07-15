package com.baidu.beidou.navi.exception.rpc;

/**
 * ClassName: CodecException <br/>
 * Function: 编解码异常
 * 
 * @author Zhang Xu
 */
public class CodecException extends RpcException {

    private static final long serialVersionUID = -6280386297535195039L;

    public CodecException() {
        super();
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }

}
