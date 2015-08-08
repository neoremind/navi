package com.baidu.beidou.sample.portal.common.constant;

/**
 * 
 * WEB用返回Json的状态码
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-4-22 下午4:19:39
 */
public class WebResponseConstant {

	public static final int RESPONSE_STATUS_OK = 0;
	public static final int RESPONSE_STATUS_SYSTEM_ERROR = 1;  	//前端直接打印statusInfo.global信息
	public static final int RESPONSE_STATUS_BUSINESS_ERROR = 2; //需要按照前后端接口定义打印错误信息
	public static final int RESPONSE_STATUS_AUTH_DENIED = 126; 		
	public static final int RESPONSE_STATUS_NOT_LOGIN = 127; 		
	
	public static final String MESSAGE_GLOBAL = "global";
	public static final String MESSAGE_FIELD = "field";
	
}
