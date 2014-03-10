package net.neoremind.navi.exception.rpc;


public class InvocationProblemOccurredException extends RpcException {
	
	private static final long serialVersionUID = 9029113871648899300L;

	public InvocationProblemOccurredException() {
	}

	public InvocationProblemOccurredException(String message) {
		super(message);
	}

	public InvocationProblemOccurredException(Throwable cause) {
		super(cause);
	}

	public InvocationProblemOccurredException(String message, Throwable cause) {
		super(message, cause);
	}

}
