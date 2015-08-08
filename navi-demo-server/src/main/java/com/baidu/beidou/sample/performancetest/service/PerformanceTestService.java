package com.baidu.beidou.sample.performancetest.service;

import java.util.Map;

import com.baidu.beidou.sample.performancetest.service.vo.Person;

public interface PerformanceTestService {

	public String receiveAndReturn(String input);
	
	public Map<Integer, Person> getPerson(Map<Integer, Person> personMap);
	
}
