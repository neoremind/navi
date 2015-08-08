package com.baidu.beidou.sample.portal.common.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.baidu.beidou.sample.portal.common.constant.WebResponseConstant;
import com.baidu.beidou.sample.portal.common.vo.JsonObject;

public class BaseController implements ApplicationContextAware {
	
	protected ApplicationContext context;

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.context = arg0;
	}
	
	protected String getMessage(String resourceMessage) {
		String message = context.getMessage(resourceMessage, null, Locale.SIMPLIFIED_CHINESE);
		return message;
	}
	
	protected <T> JsonObject buildSuccessAndCache(String key, T value) {
		JsonObject json = new JsonObject();
		json.addData(key, value);
		json.addData("cache", 1);
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_OK);
		return json;
	}
	
	protected <T> JsonObject buildSuccess(String key, T value) {
		JsonObject json = new JsonObject();
		json.addData(key, value);
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_OK);
		return json;
	}
	
	protected <T> JsonObject buildListSuccess(String key, List<?> value, int totalCnt, int pageSize) {
		JsonObject json = new JsonObject();
		json.addData(key, value);
		int totalPage = 0;
		if(totalCnt >= 0 && pageSize > 0){
			totalPage = (totalCnt / pageSize) + 1;
		}		
		
		json.addData("totalPage", totalPage);
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_OK);
		return json;
	}
	
	protected JsonObject paramError(String field, String message){
		Map<String, Object> errors = new HashMap<String, Object>();
		errors.put(field, message);
		return paramError(errors);
	}
	
	protected JsonObject sysError(){
		JsonObject json = new JsonObject();
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_SYSTEM_ERROR);
		return json;
	}
	
	protected JsonObject sysError(String msg){
		JsonObject json = new JsonObject();
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_SYSTEM_ERROR);
		
		Map<String, Object> errors = new HashMap<String, Object>();
		errors.put("global", msg);
		json.setStatusInfo(errors);
		return json;
	}
	
	protected JsonObject paramError(Map<String, Object> errors){
		JsonObject json = new JsonObject();
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_BUSINESS_ERROR);
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("field", errors);
		json.setStatusInfo(error);
		return json;
	}
	
	protected JsonObject businessError(String message){
		JsonObject json = new JsonObject();
		json.setMsg(Arrays.asList(message));
		json.addData("msg", message);
		json.setStatus(WebResponseConstant.RESPONSE_STATUS_BUSINESS_ERROR);
		return json;
	}
	
	@InitBinder
	protected void dateBinder(WebDataBinder binder) {
	            //The date format to parse or output your dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            //Create a new CustomDateEditor
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	            //Register it as custom editor for the Date type
	    binder.registerCustomEditor(Date.class, editor);
	}

}
