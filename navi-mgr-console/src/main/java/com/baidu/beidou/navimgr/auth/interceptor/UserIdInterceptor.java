package com.baidu.beidou.navimgr.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.interceptor.WebCommonInterceptor;
import com.baidu.beidou.navimgr.util.ThreadContext;

/**
 * ClassName: UserIdInterceptor <br/>
 * Function: 登陆访问者拦截器
 * 
 * @author Zhang Xu
 */
public class UserIdInterceptor extends WebCommonInterceptor {

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String userIdStr = request.getParameter(UserWebConstant.USERID);
        if (StringUtils.isEmpty(userIdStr)) {
            returnJsonSystemError(request, response, GlobalResponseStatusMsg.PARAM_MISS_ERROR);
            return false;
        }

        Integer userId = null;
        if (!StringUtils.isNumeric(userIdStr)) {
            returnJsonSystemError(request, response, GlobalResponseStatusMsg.PARAM_TYPE_ERROR);
            return false;
        }

        userId = Integer.parseInt(userIdStr);
        ThreadContext.putContext(UserWebConstant.CTX_USERID, userId);
        return true;
    }

}
