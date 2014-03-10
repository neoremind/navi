package net.neoremind.navi.exception.rpc;

/**
 * Base rpc exception <br>
 * All exception should inherit this super exception and define an error code
 * 
 * @see net.neoremind.navi.constant.NaviStatus;
 * 
 * @author Zhang Xu
 */
public abstract class RpcException extends RuntimeException {

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
