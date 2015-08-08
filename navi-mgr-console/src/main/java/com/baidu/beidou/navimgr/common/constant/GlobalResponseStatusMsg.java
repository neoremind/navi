package com.baidu.beidou.navimgr.common.constant;

import com.baidu.unbiz.biz.result.ResultCode;
import com.baidu.unbiz.biz.result.ResultCodeMessage;
import com.baidu.unbiz.biz.result.ResultCodeUtil;

/**
 * ClassName: GlobalResponseStatusMsg <br/>
 * Function: 默认web返回码和信息
 * 
 * @author Zhang Xu
 */
public enum GlobalResponseStatusMsg implements ResultCode {

    /** 成功 */
    OK,

    /** 服务器繁忙。前端直接打印statusInfo.global信息 */
    SYSTEM_BUSY,

    /** 服务器内部错误。前端直接打印statusInfo.global信息 */
    SYSTEM_ERROR,

    /** 系统业务错误。需要按照前后端接口定义打印错误信息 */
    BIZ_ERROR,

    /** 无权限操作 */
    AUTH_DENIED,

    /** 用户未登录 */
    NOT_LOGIN,
    
    /** 用户名或密码错误 */
    LOGIN_FAILED,

    /** 请求参数错误 */
    PARAM_ERROR,

    /** 请求参数类型错误 */
    PARAM_TYPE_ERROR,

    /** 请求参数绑定发生错误 */
    PARAM_BIND_ERROR,

    /** 请求参数缺失错误 */
    PARAM_MISS_ERROR,

    /** 请求方法错误 */
    REQUEST_HTTP_METHOD_ERROR,

    /** 无法获取信息页面{0} */
    GET_HTML_PAGE_FAILED,

    /** 连接Zookeeper失败,reason={0} */
    GET_ZOO_TREE_FAILED;

    private final ResultCodeUtil util = new ResultCodeUtil(this);

    @Override
    public String getName() {
        return util.getName();
    }

    @Override
    public ResultCodeMessage getMessage() {
        return util.getMessage();
    }

    public ResultCodeMessage getMessage(Object... msg) {
        return util.getMessage(msg);
    }

    @Override
    public int getCode() {
        return util.getCode();
    }

}
