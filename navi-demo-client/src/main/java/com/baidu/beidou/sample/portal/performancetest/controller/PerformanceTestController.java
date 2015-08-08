package com.baidu.beidou.sample.portal.performancetest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.beidou.sample.performancetest.service.PerformanceTestService;
import com.baidu.beidou.sample.performancetest.service.vo.BankAccount;
import com.baidu.beidou.sample.performancetest.service.vo.Job;
import com.baidu.beidou.sample.performancetest.service.vo.Person;
import com.baidu.beidou.sample.portal.common.controller.BaseController;
import com.baidu.beidou.sample.portal.common.vo.JsonObject;
import com.baidu.beidou.sample.util.RandomUtils;

@Controller
@RequestMapping("/performance")
public class PerformanceTestController extends BaseController {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(PerformanceTestController.class);
	
	@Resource
	private PerformanceTestService performanceTestService;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject get(@RequestParam(value = "num") Integer num,
			@RequestParam(value = "size") Integer size,
			@RequestParam(value = "kb") Integer kb) throws Exception {
		double avgTime = testConcurrentCall(num, size, kb);
		return buildSuccess("avgCostTime", avgTime);
	}
	
	/**
	 * 
	 * @param num invoke number
	 * @param size thread size 
	 * @param pojoNum pojo number
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/testpojo", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject testpojo(@RequestParam(value = "num") Integer num,
			@RequestParam(value = "size") Integer size,
			@RequestParam(value = "pojoNum") Integer pojoNum) throws Exception {
		Map<Integer, Person> input = new HashMap<Integer, Person>();
		for (int i = 0; i < pojoNum; i++) {
			Job job = new Job("baidu#" + i, "mananger#" + i, new Long(10000 + i));
			List<BankAccount> accountList = new ArrayList<BankAccount>();
			if (pojoNum % 5 == 0) {
				BankAccount bankAccount = new BankAccount(i, "工商银行" + i, 100 + i, new Date());
				accountList.add(bankAccount);
			}
			Person person = new Person(i, (byte) 0, "名字#" + i, i + "#地址", "123456#" + i, new Date(), job, accountList);
			input.put(person.getId(), person);
		}
		double avgTime = testConcurrentCallPojo(num, size, input);
		return buildSuccess("avgCostTime", avgTime);
	}

	public double testConcurrentCallPojo(int invokeNum, int concurrentThreadSize, Map<Integer, Person> input) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreadSize);
		CompletionService<Double> completionService = new ExecutorCompletionService<Double>(executor);
		for (int i = 0; i < invokeNum; i++) {
			completionService.submit(new TaskPojo(input));
		}
		int allElaspedTime = 0;
		for (int j = 0; j < invokeNum; j++) {
			Future<Double> future = completionService.take();
			//System.out.println(future.get());
			allElaspedTime += future.get();
		}
		System.out.println("Average cost time(ms/1000):" + new Double(allElaspedTime / invokeNum));
		executor.shutdown();
		return new Double(allElaspedTime / invokeNum);
	}
	
	public double testConcurrentCall(int invokeNum, int concurrentThreadSize, int kb) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreadSize);
		CompletionService<Double> completionService = new ExecutorCompletionService<Double>(executor);
		for (int i = 0; i < invokeNum; i++) {
			completionService.submit(new Task(kb));
		}
		int allElaspedTime = 0;
		for (int j = 0; j < invokeNum; j++) {
			Future<Double> future = completionService.take();
			//System.out.println(future.get());
			allElaspedTime += future.get();
		}
		System.out.println("Average cost time(ms/1000):" + new Double(allElaspedTime / invokeNum));
		executor.shutdown();
		return new Double(allElaspedTime / invokeNum);
	}

	class Task implements Callable<Double> {

		int kb;
		String input = RandomUtils.generateString(kb);
		
		public Task(int kb) {
			super();
			input = RandomUtils.generateString(kb * 1024);
		}

		@Override
		public Double call() throws Exception {
			long begintime = System.nanoTime();
			performanceTestService.receiveAndReturn(input);
			long endtinme = System.nanoTime();
			//System.out.println(output);
			//System.out.println("Cost millseconds:" + (endtinme - begintime));
			return (endtinme - begintime)/1000d;
		}
	}
	
	class TaskPojo implements Callable<Double> {

		Map<Integer, Person> input;
		
		public TaskPojo(Map<Integer, Person> input) {
			super();
			this.input = input;
		}

		@Override
		public Double call() throws Exception {
			long begintime = System.nanoTime();
			performanceTestService.getPerson(input);
			long endtinme = System.nanoTime();
			//System.out.println(output);
			//System.out.println("Cost millseconds:" + (endtinme - begintime));
			return (endtinme - begintime)/1000d;
		}
	}

}
