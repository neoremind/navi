package com.baidu.beidou.navi.server.vo;

/**
 * ClassName: ResponseDTO <br/>
 * Function: 响应DTO
 * 
 * @author Zhang Xu
 */
public class ResponseDTO {

    /**
     * 请求id
     */
    private long traceId;

    /**
     * 请求结果对象
     */
    private Object result;

    /**
     * 状态码
     * 
     * @see com.baidu.beidou.navi.constant.NaviStatus
     */
    private int status;

    /**
     * 请求发生异常时候的异常对象
     */
    private Throwable error;

    /**
     * Creates a new instance of ResponseDTO.
     */
    public ResponseDTO() {

    }

    /**
     * 构建响应DTO
     * 
     * @param status
     * @param error
     * @return
     */
    public static ResponseDTO build(int status, Throwable error) {
        ResponseDTO result = new ResponseDTO();
        result.setStatus(status);
        result.setError(error);
        return result;
    }

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
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
