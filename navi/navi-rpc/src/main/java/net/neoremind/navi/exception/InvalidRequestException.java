package net.neoremind.navi.exception;


public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = -6138595796119511714L;

	public InvalidRequestException() {
	}

	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(Throwable cause) {
		super(cause);
	}

}
