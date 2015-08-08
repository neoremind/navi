package com.baidu.beidou.navimgr.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.constant.WebResponseConstant;

/**
 * ClassName: JsonObject <br/>
 * Function: 前端返回的JsonObject
 * 
 * @author Zhang Xu
 */
public class JsonObject<V> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8056464271717358209L;

    /**
     * 响应状态码
     */
    private int status = GlobalResponseStatusMsg.OK.getCode();

    /**
     * 存储数据结果
     */
    private Map<String, Object> data = null;

    /**
     * 存储全局错误信息或者字段错误信息
     */
    private Map<String, Object> statusInfo = null;

    /**
     * 返回消息
     */
    private List<String> msg = null;

    /**
     * Creates a new instance of JsonObject.
     */
    public JsonObject() {
        this.data = new HashMap<String, Object>();
        this.statusInfo = new HashMap<String, Object>();
        this.msg = new ArrayList<String>();
    }

    /**
     * Creates a new instance of JsonObject.
     * 
     * @return JsonObject
     */
    public static <V> JsonObject<V> create() {
        return new JsonObject<V>();
    }

    /**
     * 添加全局消息
     * 
     * @param message
     *            消息
     */
    public void addStatusInfo(String message) {
        if (message == null) {
            return;
        }
        statusInfo.put(WebResponseConstant.MESSAGE_GLOBAL, message);
    }

    /**
     * 添加结果数据
     * 
     * @param key
     *            键
     * @param value
     *            值
     */
    public JsonObject<V> addData(String key, Object value) {
        if (key == null) {
            return this;
        }
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, value);
        return this;
    }

    /**
     * 添加字段错误信息
     * 
     * @param key
     *            键
     * @param value
     *            值
     */
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
        fieldMsgs = (Map<String, String>) (this.statusInfo.get(WebResponseConstant.MESSAGE_FIELD));
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
