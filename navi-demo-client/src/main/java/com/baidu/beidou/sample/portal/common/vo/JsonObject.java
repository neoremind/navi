package com.baidu.beidou.sample.portal.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.sample.portal.common.constant.WebResponseConstant;

/**
 * 
 * 前端返回JsonObject
 *
 * @author <a href="mailto:zhangxu04@baidu.com">Zhang Xu</a>
 * @version 2013-3-28 下午3:42:18
 */
public class JsonObject implements Serializable {

	private static final long serialVersionUID = 8056464271717358209L;

	private int status = WebResponseConstant.RESPONSE_STATUS_OK;

	private Map<String, Object> data = null;
	
	// 存储全局错误信息或者字段错误信息
	private Map<String, Object> statusInfo = null;
	
	private List<String> msg = null;

	public JsonObject() {
		status = 0;
		this.data = new HashMap<String, Object>();
		this.statusInfo = new HashMap<String, Object>();
		this.msg = new ArrayList<String>();
	}

	public void addStatusInfo(String message) {
		if (message == null) {
			return;
		}
		statusInfo.put(WebResponseConstant.MESSAGE_GLOBAL, message);
	}

	public void addData(String key, Object value) {
		if (key == null) {
			return;
		}
		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}
		this.data.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void addFieldMsg(String key, String value) {
		if (key == null) {
			return;
		}
		Map<String, String> fieldMsgs = null;
		// 字段错误信息
		if (!this.statusInfo.containsKey(WebResponseConstant.MESSAGE_FIELD)) {
			fieldMsgs = new HashMap<String, String>();
			this.statusInfo.put(WebResponseConstant.MESSAGE_FIELD, fieldMsgs);
		}
		fieldMsgs = (Map<String, String>)(this.statusInfo.get(WebResponseConstant.MESSAGE_FIELD));
		fieldMsgs.put(key, value);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(Map<String, Object> statusInfo) {
		this.statusInfo = statusInfo;
	}

	public List<String> getMsg() {
		return msg;
	}

	public void setMsg(List<String> msg) {
		this.msg = msg;
	}

}
