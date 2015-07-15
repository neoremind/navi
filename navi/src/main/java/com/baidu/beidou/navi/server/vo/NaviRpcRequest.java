package com.baidu.beidou.navi.server.vo;

import com.baidu.beidou.navi.server.NaviRpcExporter;

/**
 * ClassName: NaviRpcRequest <br/>
 * Function: NaviRpc请求逻辑对象
 * 
 * @author Zhang Xu
 */
public class NaviRpcRequest {

    /**
     * 服务接口以及实现定义
     */
    private NaviRpcExporter exporter;

    /**
     * 请求字节码
     */
    private byte[] request;

    /**
     * 请求DTO
     */
    private RequestDTO requestDTO;

    /**
     * Creates a new instance of NaviRpcRequest.
     * 
     * @param exporter
     * @param request
     */
    public NaviRpcRequest(NaviRpcExporter exporter, byte[] request) {
        this.exporter = exporter;
        this.request = request;
    }

    public NaviRpcExporter getExporter() {
        return exporter;
    }

    public void setExporter(NaviRpcExporter exporter) {
        this.exporter = exporter;
    }

    public byte[] getRequest() {
        return request;
    }

    public void setRequest(byte[] request) {
        this.request = request;
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

}