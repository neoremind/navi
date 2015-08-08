package com.baidu.beidou.sample.performancetest;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.sample.performancetest.service.PerformanceTestService;
import com.baidu.beidou.sample.util.RandomUtils;

@ContextConfiguration(locations = { "classpath:applicationContext-test-annotation.xml" })
public class PerformanceTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private PerformanceTestService performanceTestService;

	public void testReceiveAndReturn(int length) {
		String input = RandomUtils.generateString(length);
		long begintime = System.currentTimeMillis();
		performanceTestService.receiveAndReturn(input);
		long endtinme = System.currentTimeMillis();
		//System.out.println(output);
		System.out.println("Cost millseconds:" + (endtinme - begintime));
	}

	@Test
	public void testConcurrentCall() throws Exception {
		testConcurrentCall(1000, 1);
	}

	public void testConcurrentCall(int invokeNum, int concurrentThreadSize) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreadSize);
		CompletionService<Double> completionService = new ExecutorCompletionService<Double>(executor);
		for (int i = 0; i < invokeNum; i++) {
			completionService.submit(new Task(1));
		}
		int allElaspedTime = 0;
		for (int j = 0; j < invokeNum; j++) {
			Future<Double> future = completionService.take();
			//System.out.println(future.get());
			allElaspedTime += future.get();
		}
		System.out.println("Average cost time(weimiao):" + new Double(allElaspedTime / invokeNum));
		executor.shutdown();
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

	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new FileSystemXmlApplicationContext("classpath*:/applicationContext-test.xml");
		PerformanceTest performanceTest = (PerformanceTest) (ac.getBean("performanceTest"));
		int invokeNum = Integer.parseInt(args[0]);
		int concurrentThreadSize = Integer.parseInt(args[1]);
		performanceTest.testConcurrentCall(invokeNum, concurrentThreadSize);
		Thread.sleep(10000);
	}

}
