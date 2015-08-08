package com.baidu.beidou.navimgr.auth.constant;

import java.util.Map;

import com.baidu.beidou.navimgr.auth.vo.User;

/**
 * ClassName: UserWebConstant <br/>
 * Function: web-ui的用户常量
 * 
 * @author zhangxu04
 */
public class UserWebConstant {

    /**
     * 当前登入用户绑定信息的key
     */
    public static final String CTX_VISITOR = "ctx_visitor";

    /**
     * 当前登入用户ID绑定信息的key
     */
    public static final String CTX_USERID = "ctx_userId";

    /**
     * 访问起始时间的key
     */
    public static final String CTX_START_TIME = "ctx_startTime";

    /**
     * 用户ID
     */
    public static final String USERID = "userId";

    /**
     * 仅用于开发,测试环境设置为false, 便于访问排查问题
     */
    public static boolean ENABLE_AUTH = false;

    /**
     * 用户id到用户的信息
     */
    public static Map<String, User> USERMAP;

    /**
     * 用户权限
     * <ul>
     * <li>zoo_online</li>
     * <li>zoo_offline</li>
     * </ul>
     */
    public static enum UserAuth {
        /**
         * zoo online
         */
        ZOO_ONLINE("zoo_online"),

        /**
         * zoo offline
         */
        ZOO_OFFLINE("zoo_offline");

        private UserAuth(final String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * cookie键
     * <ul>
     * <li>navi_mgr_u:用户名</li>
     * <li>navi_mgr_p:密码md5</li>
     * </ul>
     */
    public static enum CookieKey {
        /**
         * 用户名
         */
        USERNAME("navi_mgr_u"),

        /**
         * 密码md5
         */
        PWDMD5("navi_mgr_p");

        private CookieKey(final String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * cookie过期时间常量
     */
    public static enum CookieExpireInHour {
        /**
         * 7天
         */
        DAY_IN_7(24 * 7),

        /**
         * 1天
         */
        DAY_IN_1(24);

        /**
         * 根据是否记住密码获取过期时间
         * 
         * @param remember
         *            是否记住密码
         * @return 过去时间
         */
        public static int getExpireInHour(boolean remember) {
            if (remember) {
                return DAY_IN_7.getValue();
            }
            return DAY_IN_1.getValue();
        }

        private CookieExpireInHour(final int value) {
            this.value = value;
        }

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public boolean isENABLE_AUTH() {
        return ENABLE_AUTH;
    }

    public void setENABLE_AUTH(boolean eNABLE_AUTH) {
        ENABLE_AUTH = eNABLE_AUTH;
    }

    public Map<String, User> getUSERMAP() {
        return USERMAP;
    }

    public void setUSERMAP(Map<String, User> uSERMAP) {
        USERMAP = uSERMAP;
    }

}
