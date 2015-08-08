package com.baidu.beidou.navimgr.auth;

import org.apache.commons.lang.StringUtils;

import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;

/**
 * ClassName: LoginValidator <br/>
 * Function: 登陆验证器
 * 
 * @author Zhang Xu
 */
public class LoginValidator {

    /**
     * 是否合法用户
     * 
     * @param username
     *            用户名
     * @param password
     *            密码md5
     * @return 是否合法
     */
    public static boolean isValid(String username, String password) {
        return StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)
                && UserWebConstant.USERMAP.containsKey(username)
                && UserWebConstant.USERMAP.get(username).getPasswordMd5().equals(password);
    }
}
