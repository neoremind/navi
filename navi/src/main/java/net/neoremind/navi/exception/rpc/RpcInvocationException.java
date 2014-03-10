package net.neoremind.navi.exception.rpc;


public class RpcInvocationException extends RpcException {
	
	private static final long serialVersionUID = 3555687591511326137L;

	public RpcInvocationException() {
	}

	public RpcInvocationException(String message) {
		super(message);
	}

	public RpcInvocationException(Throwable cause) {
		super(cause);
	}

	public RpcInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

}
