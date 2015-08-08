package com.baidu.beidou.navimgr.auth.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navimgr.auth.LoginValidator;
import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.interceptor.AbstractExcludePatternsInterceptor;
import com.baidu.beidou.navimgr.util.CookieUtils;
import com.baidu.beidou.navimgr.util.ThreadContext;

/**
 * ClassName: LoginInterceptor <br/>
 * Function: 登陆校验拦截器
 *
 * @author zhangxu04
 */
public class LoginInterceptor extends AbstractExcludePatternsInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * 不用json返回的請求PATH
     */
    private List<String> notJsonPathList;

    /**
     * Creates a new instance of LoginInterceptor.
     *
     * @param excludePatterns 排除url
     */
    public LoginInterceptor(String[] excludePatterns) {
        super(excludePatterns);
    }

    /**
     * @see com.baidu.beidou.navimgr.common.interceptor.AbstractExcludePatternsInterceptor#doPreHandle(javax.servlet
     * .http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean doPreHandle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        String requestPath = request.getRequestURI();

        // 验证是否JSON请求
        boolean jsonEnabled = true;
        if (notJsonPathList != null && notJsonPathList.contains(requestPath)) {
            jsonEnabled = false;
        }

        if (UserWebConstant.ENABLE_AUTH) {
            String cookieValueUsername = CookieUtils.getCookieValue(request,
                    UserWebConstant.CookieKey.USERNAME.getValue());
            String cookieValuePwd = CookieUtils.getCookieValue(request,
                    UserWebConstant.CookieKey.PWDMD5.getValue());
            if (!LoginValidator.isValid(cookieValueUsername, cookieValuePwd)) {
                if (jsonEnabled) {
                    returnJsonSystemError(request, response, GlobalResponseStatusMsg.NOT_LOGIN);
                } else {
                    LOG.warn("Not login, now will redirect...");
                    response.sendRedirect("/login.html");
                }
                return false;
            }
            ThreadContext.putSessionVisitor(UserWebConstant.USERMAP.get(cookieValueUsername));
        }

        return true;
    }

    /**
     * @return the notJsonPathList
     */
    public List<String> getNotJsonPathList() {
        return notJsonPathList;
    }

    /**
     * @param notJsonPathList the notJsonPathList to set
     */
    public void setNotJsonPathList(List<String> notJsonPathList) {
        this.notJsonPathList = notJsonPathList;
    }

}
