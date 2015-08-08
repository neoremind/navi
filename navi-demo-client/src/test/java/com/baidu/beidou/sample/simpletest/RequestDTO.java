package com.baidu.beidou.sample.simpletest;

/**
 * Request
 * 
 * @author Zhang Xu
 */
public class RequestDTO {

    private long traceId;

    private String method;

    private Object[] parameters;

    private String[] paramterTypes;

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

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String[] getParamterTypes() {
        return paramterTypes;
    }

    public void setParamterTypes(String[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

}
