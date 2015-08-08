package com.baidu.beidou.navimgr.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.beidou.navimgr.auth.LoginValidator;
import com.baidu.beidou.navimgr.auth.constant.UserWebConstant;
import com.baidu.beidou.navimgr.auth.constant.UserWebConstant.CookieExpireInHour;
import com.baidu.beidou.navimgr.common.constant.GlobalResponseStatusMsg;
import com.baidu.beidou.navimgr.common.controller.BaseController;
import com.baidu.beidou.navimgr.common.vo.JsonObject;
import com.baidu.beidou.navimgr.util.CookieUtils;

/**
 * ClassName: LoginController <br/>
 * Function: 登陆相关，不走拦截器
 *
 * @author Zhang Xu
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码md5
     * @param remember 是否记住密码
     * @param response 返回response
     *
     * @return JsonObject
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ResponseBody
    public JsonObject<?> login(@RequestParam(value = "username", required = true) String username,
                               @RequestParam(value = "password", required = true) String password,
                               @RequestParam(value = "remember", required = true) boolean remember,
                               HttpServletResponse response) {
        JsonObject<?> ret = JsonObject.create();
        if (LoginValidator.isValid(username, password)) {
            CookieUtils.setCookieValue(response, UserWebConstant.CookieKey.USERNAME.getValue(),
                    username, CookieExpireInHour.getExpireInHour(remember));
            CookieUtils.setCookieValue(response, UserWebConstant.CookieKey.PWDMD5.getValue(),
                    password, CookieExpireInHour.getExpireInHour(remember));
            LOG.info("Logon with username=" + username);
            ret.addData("id", UserWebConstant.USERMAP.get(username).getUserId());
            ret.addData("name", username);
        } else {
            ret.setStatus(GlobalResponseStatusMsg.LOGIN_FAILED.getCode());
            ret.addStatusInfo(GlobalResponseStatusMsg.LOGIN_FAILED.getMessage().getMessage());
        }
        return ret;
    }

    /**
     * 退出
     *
     * @param response 返回response
     *
     * @return JsonObject
     */
    @RequestMapping(value = "/userLogout", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject<?> logout(HttpServletResponse response) {
        JsonObject<?> ret = JsonObject.create();
        CookieUtils.resetCookieValue(response, UserWebConstant.CookieKey.USERNAME.getValue());
        CookieUtils.resetCookieValue(response, UserWebConstant.CookieKey.PWDMD5.getValue());
        return ret;
    }

    /**
     * 查看是否有session存在
     *
     * @param request 请求request
     *
     * @return JsonObject
     */
    @RequestMapping(value = "/userSession", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject<?> session(HttpServletRequest request) {
        JsonObject<?> ret = JsonObject.create();
        String cookieValueUsername = CookieUtils.getCookieValue(request,
                UserWebConstant.CookieKey.USERNAME.getValue());
        String cookieValuePwd = CookieUtils.getCookieValue(request,
                UserWebConstant.CookieKey.PWDMD5.getValue());
        if (LoginValidator.isValid(cookieValueUsername, cookieValuePwd)) {
            LOG.info("Already logon with username=" + cookieValueUsername);
            ret.addData("id", UserWebConstant.USERMAP.get(cookieValueUsername).getUserId());
            ret.addData("name", cookieValueUsername);
            return ret;
        } else {
            ret.setStatus(GlobalResponseStatusMsg.NOT_LOGIN.getCode());
            ret.addStatusInfo(GlobalResponseStatusMsg.NOT_LOGIN.getMessage().getMessage());
        }

        return ret;
    }

}
