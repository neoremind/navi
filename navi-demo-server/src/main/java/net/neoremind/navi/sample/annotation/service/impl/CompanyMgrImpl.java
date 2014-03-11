package net.neoremind.navi.sample.annotation.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import net.neoremind.navi.server.annotation.NaviRpcService;
import net.neoremind.navi.sample.annotation.service.CompanyMgr;
import net.neoremind.navi.sample.annotation.vo.Company;
import net.neoremind.navi.sample.annotation.vo.Department;

/**
 * Sample annotation company service implementing basic CRUD operations
 * 
 * @author Zhang Xu
 */
@Service
@NaviRpcService(serviceInterface = CompanyMgr.class)
public class CompanyMgrImpl implements CompanyMgr {

	private Map<Integer, Company> companyMap = new HashMap<Integer, Company>();
	private List<Company> companyList = new ArrayList<Company>();

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@PostConstruct
	public void init() {
		try {
			Department department101 = new Department(101, "R&D");
			Department department102 = new Department(102, "联盟前端技术部.");
			Department department103 = new Department(103, "市场部");
			Department department104 = new Department(104, "Sales");
			List<Department> departmentList1 = new ArrayList<Department>();
			departmentList1.add(department101);
			departmentList1.add(department102);
			departmentList1.add(department103);
			departmentList1.add(department104);

			Department department201 = new Department(201, "行政部");
			Department department202 = new Department(202, "SaaS云");
			List<Department> departmentList2 = new ArrayList<Department>();
			departmentList2.add(department201);
			departmentList2.add(department202);

			Company company1 = new Company(88, "百度时代网络技术(北京)有限公司", dateFormat.parse("2000-11-11"), departmentList1);
			Company company2 = new Company(99, "IBM China", dateFormat.parse("1956-5-6"), departmentList2);

			companyList.add(company1);
			companyList.add(company2);

			companyMap.put(88, company1);
			companyMap.put(99, company2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * query
	 */
	public Company get(int id) {
		return companyMap.get(id);
	}

	public List<Company> getAll() {
		return companyList;
	}

	/**
	 * add
	 */
	public Company add(Company company) {
		if (company.getId() == null || company.getId() <= 0) {
			throw new RuntimeException("Company id invliad");
		}
		if (companyMap.containsKey(company.getId())) {
			throw new RuntimeException("Company id deplicate");
		}
		companyMap.put(company.getId(), company);
		companyList.add(company);
		return company;
	}

	/**
	 * delete
	 */
	public void delete(int id) {
		if (!companyMap.containsKey(id)) {
			throw new RuntimeException("Company id not exist");
		}
		companyList.remove(companyMap.get(id));
		companyMap.remove(id);
	}

	/**
	 * update
	 */
	public void update(Company company) {
		if (company.getId() == null || company.getId() <= 0) {
			throw new RuntimeException("Company id invliad");
		}
		if (!companyMap.containsKey(company.getId())) {
			throw new RuntimeException("Company id not exist");
		}
		companyMap.put(company.getId(), company);
	}

}
