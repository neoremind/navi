package com.baidu.beidou.navi.it.service.impl;

import org.springframework.stereotype.Service;

import com.baidu.beidou.navi.it.service.ProxyService;
import com.baidu.beidou.navi.server.annotation.NaviRpcService;

/**
 * ClassName: ProxyServiceImpl <br/>
 * Function: 测试利用spring框架做代理，例如cglib来拦截方法，navi-rpc是否work
 * 
 * @author Zhang Xu
 */
@Service
@NaviRpcService(serviceInterface = ProxyService.class)
public class ProxyServiceImpl implements ProxyService {

    @Override
    public String hellworld(String input) {
        return input;
    }

}
