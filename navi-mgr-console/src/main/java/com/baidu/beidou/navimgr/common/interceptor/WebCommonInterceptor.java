package com.baidu.beidou.navimgr.common.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.beidou.navimgr.common.constant.WebResponseConstant;
import com.baidu.beidou.navimgr.common.vo.JsonObject;
import com.baidu.beidou.navimgr.util.JsonUtils;
import com.baidu.unbiz.biz.result.ResultCode;

/**
 * ClassName: WebCommonInterceptor <br/>
 * Function: web ui公共拦截器
 * 
 * @author Zhang Xu
 */
public class WebCommonInterceptor extends HandlerInterceptorAdapter {

    /**
     * 当拦截器内发生错误时，返回json格式的错误信息
     * 
     * @param request
     *            请求request
     * @param response
     *            返回response
     * @param message
     *            错误消息
     * @throws IOException
     */
    protected void returnJsonSystemError(HttpServletRequest request, HttpServletResponse response,
            ResultCode resultCode) throws IOException {
        Map<String, Object> errors = new HashMap<String, Object>();
        errors.put(WebResponseConstant.MESSAGE_GLOBAL, resultCode.getMessage().getMessage());
        JsonObject<?> json = JsonObject.create();
        json.setStatus(resultCode.getCode());
        json.setStatusInfo(errors);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtils.toJson(json));
    }

}
