package com.baidu.beidou.navimgr.common.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.constant.WebResponseConstant;
import com.baidu.beidou.navimgr.common.vo.JsonObject;

/**
 * ClassName: BaseController <br/>
 * Function: 所有Spring MVC Controller父类
 * 
 * @author Zhang Xu
 */
@SuppressWarnings("rawtypes")
public class BaseController implements ApplicationContextAware {

    /**
     * context
     */
    protected ApplicationContext context;

    /**
     * jackson json mapper
     */
    protected ObjectMapper mapper;

    /**
     * init
     */
    @PostConstruct
    public void init() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.ALWAYS);
    }

    /**
     * 构造带前端缓存的成功结果
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @return JsonObject
     */
    protected <T> JsonObject buildSuccessAndCache(String key, T value) {
        JsonObject json = JsonObject.create();
        json.addData(key, value);
        json.addData(WebResponseConstant.MESSAGE_CACHE, 1);
        json.setStatus(GlobalResponseStatusMsg.OK.getMessage().getCode());
        return json;
    }

    /**
     * 构造成功结果
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @return JsonObject
     */
    protected <T> JsonObject buildSuccess(String key, T value) {
        JsonObject json = JsonObject.create();
        json.addData(key, value);
        json.setStatus(GlobalResponseStatusMsg.OK.getMessage().getCode());
        return json;
    }

    /**
     * 构造list成功结果
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param totalCnt
     *            总数量
     * @param pageSize
     *            页数
     * @return JsonObject
     */
    protected <T> JsonObject buildListSuccess(String key, List<?> value, int totalCnt, int pageSize) {
        JsonObject json = JsonObject.create();
        json.addData(key, value);
        int totalPage = 0;
        if (totalCnt >= 0 && pageSize > 0) {
            totalPage = (totalCnt / pageSize) + 1;
        }

        json.addData("totalPage", totalPage);
        json.setStatus(GlobalResponseStatusMsg.OK.getMessage().getCode());
        return json;
    }

    /**
     * 构造参数错误
     * 
     * @param field
     *            错误属性
     * @param message
     *            错误消息
     * @return JsonObject
     */
    protected JsonObject paramError(String field, String message) {
        Map<String, Object> errors = new HashMap<String, Object>();
        errors.put(field, message);
        return paramError(errors);
    }

    /**
     * 构造不带消息的系统错误
     * 
     * @return JsonObject
     */
    protected JsonObject sysError() {
        JsonObject json = JsonObject.create();
        json.setStatus(GlobalResponseStatusMsg.SYSTEM_ERROR.getMessage().getCode());
        json.addStatusInfo(GlobalResponseStatusMsg.SYSTEM_ERROR.getMessage().getMessage());
        return json;
    }

    /**
     * 构造带有消息的系统错误
     * 
     * @param msg
     *            错误消息
     * @return JsonObject
     */
    @SuppressWarnings("unchecked")
    protected JsonObject sysError(String msg) {
        JsonObject json = JsonObject.create();
        json.setStatus(GlobalResponseStatusMsg.SYSTEM_ERROR.getMessage().getCode());
        Map<String, Object> errors = new HashMap<String, Object>();
        errors.put(WebResponseConstant.MESSAGE_GLOBAL, msg);
        json.setStatusInfo(errors);
        return json;
    }

    /**
     * 构造参数错误
     * 
     * @param errors
     *            错误胸袭
     * @return JsonObject
     */
    @SuppressWarnings("unchecked")
    protected JsonObject paramError(Map<String, Object> errors) {
        JsonObject json = JsonObject.create();
        json.setStatus(GlobalResponseStatusMsg.SYSTEM_ERROR.getMessage().getCode());
        Map<String, Object> error = new HashMap<String, Object>();
        error.put(WebResponseConstant.MESSAGE_FIELD, errors);
        json.setStatusInfo(error);
        return json;
    }

    /**
     * 构造系统业务错误
     * 
     * @param message
     *            错误消息
     * @return JsonObject
     */
    @SuppressWarnings("unchecked")
    protected JsonObject bizError(String message) {
        JsonObject json = JsonObject.create();
        json.setMsg(Arrays.asList(message));
        json.addData(WebResponseConstant.MESSAGE_MSG, message);
        json.setStatus(GlobalResponseStatusMsg.BIZ_ERROR.getMessage().getCode());
        return json;
    }

    @InitBinder
    protected void dateBinder(WebDataBinder binder) {
        // The date format to parse or output your dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Create a new CustomDateEditor
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        // Register it as custom editor for the Date type
        binder.registerCustomEditor(Date.class, editor);
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

}
