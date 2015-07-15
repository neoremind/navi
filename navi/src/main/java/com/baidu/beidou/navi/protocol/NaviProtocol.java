package com.baidu.beidou.navi.protocol;

/**
 * ClassName: NaviProtocol <br/>
 * Function: Navi所支持的序列化协议枚举
 * 
 * @author Zhang Xu
 */
public enum NaviProtocol {

    /**
     * protostuff
     */
    PROTOSTUFF("application/baidu.protostuff"),

    /**
     * protobuf
     */
    PROTOBUF("application/baidu.protobuf"),

    /**
     * json
     */
    JSON("application/baidu.json");

    /**
     * content type名称
     */
    private String name;

    /**
     * Creates a new instance of NaviProtocol.
     * 
     * @param name
     */
    private NaviProtocol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
