package com.baidu.beidou.navi.client.attachment;

import com.baidu.beidou.navi.server.vo.AppIdToken;

/**
 * ClassName: Attachment <br/>
 * Function: 附加信息
 * 
 * @author Zhang Xu
 */
public class Attachment {

    /**
     * 客户端调用的appId和token
     */
    private AppIdToken appIdToken;

    public AppIdToken getAppIdToken() {
        return appIdToken;
    }

    public Attachment setAppIdToken(AppIdToken appIdToken) {
        this.appIdToken = appIdToken;
        return this;
    }

}
