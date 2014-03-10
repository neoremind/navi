package net.neoremind.navi.server.vo;

/**
 * Response
 * 
 * @author Zhang Xu
 */
public class ResponseDTO {

	private long id;

	private Object result;

	/**
	 * @see net.neoremind.navi.constant.NaviStatus
	 */
	private int status;

	private Throwable error;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

}
