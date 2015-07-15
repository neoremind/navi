package com.baidu.beidou.navi.server.vo;

/**
 * ClassName: RequestDTO <br/>
 * Function: 请求DTO
 * 
 * @author Zhang Xu
 */
public class RequestDTO {

    /**
     * 请求追踪trace id
     */
    private long traceId;

    /**
     * 请求方法名称
     */
    private String method;

    /**
     * 请求参数数组
     */
    private Object[] parameters;

    /**
     * 请求方法参数类名数组
     */
    private String[] paramterTypes;

    /**
     * appId
     */
    private String appId;

    /**
     * token
     */
    private String token;

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParamterTypes() {
        return paramterTypes;
    }

    public void setParamterTypes(String[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
