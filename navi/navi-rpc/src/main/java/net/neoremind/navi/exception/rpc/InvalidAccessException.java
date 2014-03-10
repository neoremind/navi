package net.neoremind.navi.exception.rpc;


public class InvalidAccessException extends RpcException {

	private static final long serialVersionUID = -2871706631475075323L;

	public InvalidAccessException() {
		super();
	}

	public InvalidAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccessException(String message) {
		super(message);
	}

	public InvalidAccessException(Throwable cause) {
		super(cause);
	}

}
