package com.baidu.beidou.sample.performancetest.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baidu.beidou.navi.server.annotation.NaviRpcService;
import com.baidu.beidou.sample.performancetest.service.PerformanceTestService;
import com.baidu.beidou.sample.performancetest.service.vo.Person;

@Service
@NaviRpcService(serviceInterface = PerformanceTestService.class)
public class PerformanceTestServiceImpl implements PerformanceTestService {

	public String receiveAndReturn(String input) {
		return input;
	}

	public Map<Integer, Person> getPerson(Map<Integer, Person> personMap) {
		return personMap;
	}
	
}
