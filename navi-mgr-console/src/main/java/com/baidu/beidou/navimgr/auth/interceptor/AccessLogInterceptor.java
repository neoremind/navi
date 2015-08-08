package com.baidu.beidou.navimgr.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.auth.vo.User;
import com.baidu.beidou.navimgr.common.interceptor.WebCommonInterceptor;
import com.baidu.beidou.navimgr.util.HttpUtils;
import com.baidu.beidou.navimgr.util.IPUtils;
import com.baidu.beidou.navimgr.util.ThreadContext;

/**
 * ClassName: AccessLogInterceptor <br/>
 * Function: 访问日志记录拦截器，作为最外层拦截器实现，特别注意需要负责线程上下文ThreadContext的管理
 *
 * @author zhangxu04
 */
public class AccessLogInterceptor extends WebCommonInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http
     * .HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                                Exception arg3) throws Exception {
        try {
            String ip = IPUtils.getIpAddr(arg0);
            String browser = HttpUtils.getHttpBrowser(arg0);
            String url = HttpUtils.getFullURL(arg0);
            long elapsedTime = System.currentTimeMillis()
                    - (Long) (ThreadContext.getContext(UserWebConstant.CTX_START_TIME));
            LOG.info(getAccessLog(ip, getUser(), getUserId(), browser, url, elapsedTime));
        } catch (Exception e) {
            LOG.error("Failed to take down access log. " + e.getMessage(), e);
        } finally {
            ThreadContext.clean(); // clear thread context
        }
    }

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http
     * .HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        ThreadContext.init(); // init thread context
        ThreadContext.putContext(UserWebConstant.CTX_START_TIME, System.currentTimeMillis());
        return true;
    }

    /**
     * 获得访问者
     *
     * @return User
     */
    protected User getUser() {
        User visitor = ThreadContext.getSessionVisitor();
        if (visitor != null) {
            return visitor;
        }
        return null;
    }

    /**
     * 获得请求中要操作的userId
     *
     * @return 用户id
     */
    protected Integer getUserId() {
        Integer userId = ThreadContext.getContext(UserWebConstant.CTX_USERID);
        if (userId != null) {
            return userId;
        }
        return null;
    }

    /**
     * 日志操作记录-人员类型
     */
    protected static enum AccessLogType {
        /**
         * 管理员操作
         */
        MANAGER,

        /**
         * 前端用户
         */
        PORTAL_USER,
    }

    /**
     * 记录访问日志
     *
     * @param ip          访问IP
     * @param visitor     访问者
     * @param userId      用户id
     * @param browser     浏览器
     * @param url         访问URL
     * @param elapsedTime 请求耗时
     *
     * @return 记录日志行
     */
    private String getAccessLog(String ip, User visitor, Integer userId, String browser,
                                String url, long elapsedTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(null != ip ? ip : "unknown").append("\t");

        if (visitor == null) {
            sb.append("-\t-\t");
        } else {
            if (userId == null) {
                sb.append("-\t");
            } else {
                sb.append(
                        visitor.getUserId() == userId ? AccessLogType.PORTAL_USER
                                : AccessLogType.MANAGER).append("\t");
            }
            sb.append(visitor.getUserId()).append(",").append(visitor.getUserName()).append(",")
                    .append(visitor.getRoles()).append("\t");
        }

        sb.append(null != userId ? userId : "-").append("\t");
        sb.append(url).append("\t");
        sb.append(browser).append("\t");
        sb.append(elapsedTime).append("ms");
        return sb.toString();
    }

}
