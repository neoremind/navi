package net.neoremind.navi.exception;

public class InvalidStrategyException extends RuntimeException {

	private static final long serialVersionUID = -6138595796119511714L;

	public InvalidStrategyException() {
	}

	public InvalidStrategyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStrategyException(String message) {
		super(message);
	}

	public InvalidStrategyException(Throwable cause) {
		super(cause);
	}

}
