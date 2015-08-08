package com.baidu.beidou.navimgr.common.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.constant.WebResponseConstant;
import com.baidu.unbiz.biz.result.ResultCode;
import com.google.common.collect.Maps;

/**
 * ClassName: ExceptionHandler <br/>
 * Function: Spring MVC Controller和Interceptor全局异常处理
 *
 * @author Zhang Xu
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver, ApplicationContextAware,
        Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * context
     */
    protected ApplicationContext context;

    /**
     * 默认HandlerExceptionResolver优先级，设置为最高，用于覆盖系统默认的异常处理器
     */
    private int order = Ordered.HIGHEST_PRECEDENCE;

    /**
     * 全局处理错误
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or <code>null</code> if none chosen at the time of the exception (for
     *                 example,
     *                 if multipart resolution failed)
     * @param ex       the exception that got thrown during handler execution
     *
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     *
     * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http
     * .HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object o, Exception e) {
        ModelAndView model = new ModelAndView(new MappingJacksonJsonView());
        try {
            if (e instanceof TypeMismatchException) {
                LOG.warn("TypeMismatchException occurred. " + e.getMessage());
                return buildBizErrors((TypeMismatchException) e, model);
            } else if (e instanceof BindException) {
                LOG.warn("BindException occurred. " + e.getMessage());
                return buildBizErrors((BindException) e, model);
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                LOG.warn("HttpRequestMethodNotSupportedException occurred. " + e.getMessage());
                return buildError(model, GlobalResponseStatusMsg.REQUEST_HTTP_METHOD_ERROR);
            } else if (e instanceof MissingServletRequestParameterException) {
                LOG.warn("MissingServletRequestParameterException occurred. " + e.getMessage());
                return buildError(model, GlobalResponseStatusMsg.PARAM_MISS_ERROR);
            } else {
                LOG.error("System error occurred. " + e.getMessage(), e);
                return buildError(model, GlobalResponseStatusMsg.SYSTEM_ERROR);
            }
        } catch (Exception ex) {
            // Omit all detailed error message including stack trace to external user
            LOG.error("Unexpected error occurred! This should never happen! " + ex.getMessage(), ex);
            model.addObject("status", SYS_ERROR_CODE);
            model.addObject("msg", SYS_ERROR_MSG);
            return model;
        }
    }

    /**
     * 处理参数类型错误
     *
     * @param e     错误
     * @param model mvc
     *
     * @return mvc
     */
    private ModelAndView buildBizErrors(TypeMismatchException e, ModelAndView model) {
        Throwable t = e.getCause();
        if (t instanceof ConversionFailedException) {
            ConversionFailedException x = (ConversionFailedException) t;
            TypeDescriptor type = x.getTargetType();
            Annotation[] annotations = type != null ? type.getAnnotations() : new Annotation[0];
            Map<String, Object> errors = new HashMap<String, Object>();
            for (Annotation a : annotations) {
                if (a instanceof RequestParam) {
                    errors.put(((RequestParam) a).value(), GlobalResponseStatusMsg.PARAM_TYPE_ERROR
                            .getMessage().getMessage());
                }
            }
            if (errors.size() > 0) {
                return buildBizError(model, errors);
            }
        }

        return buildError(model, GlobalResponseStatusMsg.PARAM_TYPE_ERROR);
    }

    /**
     * 处理参数绑定错误
     *
     * @param be  绑定异常
     * @param mvc mvc
     *
     * @return mvc
     */
    private ModelAndView buildBizErrors(BindException be, ModelAndView mvc) {
        Map<String, Object> paramErrors = new HashMap<String, Object>();
        Object o = be.getTarget();
        for (Object error : be.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fe = (FieldError) error;
                String field = fe.getField();
                Field oField = null;
                try {
                    oField = o.getClass().getField(field);
                } catch (NoSuchFieldException e) {
                    // TODO: omit handle exception
                }
                if (oField == null) {
                    paramErrors.put(field, GlobalResponseStatusMsg.PARAM_BIND_ERROR.getMessage()
                            .getMessage());
                } else if (oField.getType().equals(Integer.class)
                        || oField.getType().equals(Long.class)
                        || oField.getType().equals(Short.class)) {
                    paramErrors.put(field, GlobalResponseStatusMsg.PARAM_BIND_ERROR.getMessage()
                            .getMessage());
                } else if (oField.getType().equals(Float.class)
                        || oField.getType().equals(Double.class)) {
                    paramErrors.put(field, GlobalResponseStatusMsg.PARAM_BIND_ERROR.getMessage()
                            .getMessage());
                } else {
                    paramErrors.put(field, GlobalResponseStatusMsg.PARAM_BIND_ERROR.getMessage()
                            .getMessage());
                }
            }
        }

        return buildBizError(mvc, paramErrors);
    }

    /**
     * 构造系统业务错误
     *
     * @param mvc         mvc
     * @param paramErrors 错误参数
     *
     * @return mvc
     */
    private ModelAndView buildBizError(ModelAndView mvc, Map<String, Object> paramErrors) {
        Map<String, Object> error = new HashMap<String, Object>();
        error.put("field", paramErrors);
        mvc.addObject("status", GlobalResponseStatusMsg.BIZ_ERROR.getCode());
        mvc.addObject("statusInfo", error);
        return mvc;
    }

    /**
     * 构造错误
     *
     * @param mvc   mvc
     * @param error 错误
     *
     * @return mvc
     */
    @SuppressWarnings("rawtypes")
    private ModelAndView buildError(ModelAndView mvc, ResultCode error) {
        mvc.addObject("data", new ArrayList(0));
        mvc.addObject("status", error.getCode());
        Map<String, Object> statusInfo = Maps.newHashMapWithExpectedSize(1);
        statusInfo.put(WebResponseConstant.MESSAGE_GLOBAL, error.getMessage().getMessage());
        mvc.addObject("statusInfo", statusInfo);
        mvc.addObject("msg", error.getMessage().getMessage());
        return mvc;
    }

    /**
     * 默认服务器内部错误返回码
     */
    private static final int SYS_ERROR_CODE = 1;

    /**
     * 默认服务器内部错误返回消息
     */
    private static final String SYS_ERROR_MSG = "服务器内部错误";

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

}
