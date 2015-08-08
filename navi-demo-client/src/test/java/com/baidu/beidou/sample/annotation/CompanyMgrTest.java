package com.baidu.beidou.sample.annotation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.sample.annotation.exception.IdDuplicateException;
import com.baidu.beidou.sample.annotation.service.CompanyMgr;
import com.baidu.beidou.sample.annotation.vo.Company;
import com.baidu.beidou.sample.annotation.vo.Department;

@ContextConfiguration(locations = { "classpath:applicationContext-test-annotation.xml" })
public class CompanyMgrTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private CompanyMgr companyMgr;

    @Test
    public void testGetAll() {
        List<Company> result = companyMgr.getAll();
        for (Company company : result) {
            System.out.println(company);
        }
        assertThat(result.size(), is(2));
    }

    @Test
    public void testGet() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(10);
        List<Thread> tList = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    Company result = companyMgr.get(88);
                    System.out.println(result);
                    assertThat(result.getName(), is("百度时代网络技术(北京)有限公司"));
                    cdl.countDown();
                }
            });
            t.start();
        }

        System.out.println("got there");
        Thread.sleep(10000);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    Company result = companyMgr.get(88);
                    System.out.println(result);
                    assertThat(result.getName(), is("百度时代网络技术(北京)有限公司"));
                    cdl.countDown();
                }
            });
            tList.add(t);
        }

        for (Thread thread : tList) {
            thread.start();
        }
        cdl.await();
    }

    @Test
    public void testGetByIds() {
        List<Company> result = companyMgr.getByIds(Arrays.asList(new Integer[] { 88, 99 }));
        System.out.println(result);
        assertThat(result.size(), is(2));
    }

    @Test
    public void testGetMapByIds() {
        Map<Integer, Company> result = companyMgr.getMapByIds(Arrays
                .asList(new Integer[] { 88, 99 }));
        System.out.println(result);
        assertThat(result.size(), is(2));
    }

    @Test
    public void testAdd() {
        Department department101 = new Department(111, "Phone");
        Department department102 = new Department(222, "Visio Studio");
        List<Department> departmentList1 = new ArrayList<Department>();
        departmentList1.add(department101);
        departmentList1.add(department102);

        Company company = new Company(114, "Mircosoft中国", new Date(), departmentList1);

        try {
            Company result = companyMgr.add(company);
            System.out.println(result);
            assertThat(result.getName(), is("Mircosoft中国"));
        } catch (IdDuplicateException e) {
            System.out.println("got id dup exception here!!!!");
            e.printStackTrace();
        }
    }

    @Test
    public void testAddMultiParams() {
        Department department101 = new Department(111, "Phone");
        Department department102 = new Department(222, "Visio Studio");
        List<Department> departmentList1 = new ArrayList<Department>();
        departmentList1.add(department101);
        departmentList1.add(department102);

        Company company = new Company(114, "Mircosoft中国", new Date(), departmentList1);

        try {
            Company result = companyMgr.add(company);
            System.out.println(result);
            assertThat(result.getName(), is("Mircosoft中国"));
        } catch (IdDuplicateException e) {
            System.out.println("got id dup exception here!!!!");
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNegative() {
        Company result = companyMgr.get(99999999);
        System.out.println(result);
        assertThat(result, nullValue());
    }

    // @Test
    // public void testGetServices() {
    // ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    // String[] beanNamesForType = context.getBeanNamesForType(NaviRpcExporter.class);
    // Map<String, Object> beanNamesWithAnnotation = context.getBeansWithAnnotation(NaviRpcService.class);
    // }

}
