package net.neoremind.navi.exception.rpc;


public class ParseErrorException extends RpcException {

	private static final long serialVersionUID = -6280386297535195039L;

	public ParseErrorException() {
		super();
	}

	public ParseErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseErrorException(String message) {
		super(message);
	}

	public ParseErrorException(Throwable cause) {
		super(cause);
	}

}
