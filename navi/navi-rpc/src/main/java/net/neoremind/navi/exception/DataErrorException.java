package net.neoremind.navi.exception;

/**
 * Zookeeper client data exception
 * 
 * @author Zhang Xu
 */
public class DataErrorException extends RuntimeException {

	private static final long serialVersionUID = 3222573813262320183L;

	public DataErrorException() {
	}

	public DataErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataErrorException(String message) {
		super(message);
	}

	public DataErrorException(Throwable cause) {
		super(cause);
	}

}
