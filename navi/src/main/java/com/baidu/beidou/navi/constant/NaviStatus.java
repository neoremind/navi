package com.baidu.beidou.navi.constant;

/**
 * ClassName: NaviStatus <br/>
 * Function: Navi内部的返回码定义
 * 
 * @author Zhang Xu
 */
public class NaviStatus {

    /**
     * 成功。
     */
    public static int RPC_OK = 0;

    /**
     * 失败。通常是框架造成的，例如序列化失败等
     */
    public static int RPC_FAIL = 1;

    /**
     * 系统内部错误。通常是业务逻辑造成的，不属于框架问题
     */
    public static int SYS_ERROR = 2;

}
