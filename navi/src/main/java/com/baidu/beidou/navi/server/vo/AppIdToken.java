package com.baidu.beidou.navi.server.vo;

import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: AppIdToken <br/>
 * Function: 进行权限控制的appId+token封装类
 * 
 * @author Zhang Xu
 */
public class AppIdToken {

    /**
     * appId
     */
    private String appId;

    /**
     * token
     */
    private String token;

    /**
     * Creates a new instance of AppIdToken.
     * 
     * @param appId
     *            APP ID
     * @param token
     *            TOKEN
     */
    public AppIdToken(String appId, String token) {
        super();
        this.appId = appId;
        this.token = token;
    }

    @Override
    public String toString() {
        return appId + StringUtil.COLON + token;
    }

    /**
     * 是否相同
     * 
     * @param appId
     *            APP ID
     * @param token
     *            TOKEN
     * @return 是否合法相同
     */
    public static boolean isValid(AppIdToken appIdToken, String appId, String token) {
        if (appIdToken == null || StringUtil.isEmpty(appId) || StringUtil.isEmpty(token)) {
            return false;
        }
        return appIdToken.appId.equals(appId) && appIdToken.token.equals(token);
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
