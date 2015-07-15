package com.baidu.beidou.navi.it.service.impl;

import org.springframework.stereotype.Service;

import com.baidu.beidou.navi.it.service.AuthService;
import com.baidu.beidou.navi.server.annotation.Authority;
import com.baidu.beidou.navi.server.annotation.NaviRpcAuth;
import com.baidu.beidou.navi.server.annotation.NaviRpcService;

@Service
@NaviRpcService(serviceInterface = AuthService.class)
@NaviRpcAuth({ @Authority(appId = "beidou", token = "123456"),
        @Authority(appId = "ssp", token = "111111") })
public class AuthServiceImpl implements AuthService {

    @Override
    public String sayHello() {
        return "hi!";
    }

}
