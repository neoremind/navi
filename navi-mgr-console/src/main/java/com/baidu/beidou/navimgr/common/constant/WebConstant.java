package com.baidu.beidou.navimgr.common.constant;


/**
 * ClassName: WebConstant <br/>
 * Function: Web UI常量定义
 * 
 * @author zhangxu04
 */
public class WebConstant {

    /**
     * URL前缀
     */
    public static final String BASE_PATH = "/navi/";

    /**
     * zookeeper相关URL前缀
     */
    public static final String ZOO_URL_PATH = BASE_PATH + "zoo";

    /**
     * 页面相关URL前缀
     */
    public static final String PAGE_URL_PATH = BASE_PATH + "page";

    /**
     * 登陆验证URL前缀
     */
    public static final String AUTH_URL_PATH = BASE_PATH + "auth";

    /**
     * 每页数量常量
     */
    public interface PAGE_SIZE {
        int PAGE_SIZE_20 = 20;
        int PAGE_SIZE_50 = 50;
        int PAGE_SIZE_100 = 100;
    }
}
