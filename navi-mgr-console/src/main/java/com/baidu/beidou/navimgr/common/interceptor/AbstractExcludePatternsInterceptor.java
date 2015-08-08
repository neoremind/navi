package com.baidu.beidou.navimgr.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

/**
 * ClassName: AbstractExcludePatternsInterceptor <br/>
 * Function: 排除urlpath处理拦截器
 * 
 * @author Zhang Xu
 */
public abstract class AbstractExcludePatternsInterceptor extends WebCommonInterceptor {

    /**
     * urlPathHelper
     */
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * pathMatcher
     */
    private PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 排除path匹配
     */
    protected final String[] excludePatterns;

    /**
     * Creates a new instance of AbstractExcludePatternsInterceptor.
     * 
     * @param excludePatterns
     *            排除path
     */
    public AbstractExcludePatternsInterceptor(String[] excludePatterns) {
        this.excludePatterns = excludePatterns;

    }

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        final String lookupPath = urlPathHelper.getLookupPathForRequest(request);

        if (this.excludePatterns != null) {
            for (String pattern : this.excludePatterns) {
                if (pathMatcher.match(pattern, lookupPath)) {
                    return true;
                }
            }
        }

        return doPreHandle(request, response, handler);
    }

    /**
     * 前置处理
     * 
     * @param request
     *            请求request
     * @param response
     *            返回response
     * @param handler
     *            处理器
     * @return 是否处理成功
     * @throws Exception
     */
    public abstract boolean doPreHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception;

}
