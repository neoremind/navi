package com.baidu.beidou.navi.server.vo;

/**
 * ClassName: NaviRpcResponse <br/>
 * Function: NaviRpc结果逻辑对象
 * 
 * @author Zhang Xu
 */
public class NaviRpcResponse {

    /**
     * 响应字节码
     */
    private byte[] response;

    /**
     * 响应DTO
     */
    private ResponseDTO responseDTO;

    /**
     * Creates a new instance of NaviRpcResponse.
     */
    public NaviRpcResponse() {

    }

    /**
     * Creates a new instance of NaviRpcResponse.
     * 
     * @param response
     */
    public NaviRpcResponse(byte[] response) {
        this.response = response;
    }

    /**
     * Creates a new instance of NaviRpcResponse.
     * 
     * @param responseDTO
     */
    public NaviRpcResponse(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public ResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public void setResponseDTO(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }

}