package net.neoremind.navi.exception.rpc;


public class DeserilizeNullException  extends RpcException {
	
	private static final long serialVersionUID = -6138595196129511714L;

	public DeserilizeNullException() {
	}

	public DeserilizeNullException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeserilizeNullException(String message) {
		super(message);
	}

	public DeserilizeNullException(Throwable cause) {
		super(cause);
	}
	
}
