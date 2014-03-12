package net.neoremind.navi.exception.rpc;


public class MethodNotFoundException extends RpcException {

	private static final long serialVersionUID = -1760095868220273812L;

	public MethodNotFoundException() {
		super();
	}

	public MethodNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MethodNotFoundException(String message) {
		super(message);
	}

	public MethodNotFoundException(Throwable cause) {
		super(cause);
	}

}
