package net.neoremind.navi.exception.rpc;


public class InvalidParamException extends RpcException {

	private static final long serialVersionUID = -2871706631475075323L;

	public InvalidParamException() {
		super();
	}

	public InvalidParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParamException(String message) {
		super(message);
	}

	public InvalidParamException(Throwable cause) {
		super(cause);
	}

}
