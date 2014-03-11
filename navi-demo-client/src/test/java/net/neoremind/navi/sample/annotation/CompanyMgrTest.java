package net.neoremind.navi.sample.annotation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import net.neoremind.navi.sample.annotation.service.CompanyMgr;
import net.neoremind.navi.sample.annotation.vo.Company;
import net.neoremind.navi.sample.annotation.vo.Department;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
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
	public void testGet() {
		Company result = companyMgr.get(88);
		System.out.println(result);
		assertThat(result.getName(), is("百度时代网络技术(北京)有限公司"));
	}
	
	@Test
	public void testAdd() {
		Department department101 = new Department(111, "Phone");
		Department department102 = new Department(222, "Visio Studio");
		List<Department> departmentList1 = new ArrayList<Department>();
		departmentList1.add(department101);
		departmentList1.add(department102);

		Company company = new Company(111, "Mircosoft中国", new Date(), departmentList1);
		
		Company result = companyMgr.add(company);
		System.out.println(result);
		assertThat(result.getName(), is("Mircosoft中国"));
	}

	@Test
	public void testGetNegative() {
		Company result = companyMgr.get(99999999);
		System.out.println(result);
		assertThat(result, nullValue());
	}

//	@Test
//	public void testGetServices() {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		String[] beanNamesForType = context.getBeanNamesForType(NaviRpcExporter.class);
//		Map<String, Object> beanNamesWithAnnotation = context.getBeansWithAnnotation(NaviRpcService.class);
//	}

}
