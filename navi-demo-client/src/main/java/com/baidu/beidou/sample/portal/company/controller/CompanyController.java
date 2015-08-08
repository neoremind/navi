package com.baidu.beidou.sample.portal.company.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.beidou.sample.annotation.service.CompanyMgr;
import com.baidu.beidou.sample.annotation.vo.Company;
import com.baidu.beidou.sample.annotation.vo.Department;
import com.baidu.beidou.sample.portal.common.controller.BaseController;
import com.baidu.beidou.sample.portal.common.vo.JsonObject;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(CompanyController.class);
	
	@Resource
	private CompanyMgr companyMgr;
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject get(@RequestParam(value = "id") Integer id) throws Exception {
		Company result = companyMgr.get(id);
		return buildSuccess("company", result);
	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject getAll() throws Exception {
		List<Company> result = companyMgr.getAll();
		return buildSuccess("company", result);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject add() throws Exception {
		Department department101 = new Department(111, "Phone");
		Department department102 = new Department(222, "Visio Studio");
		List<Department> departmentList1 = new ArrayList<Department>();
		departmentList1.add(department101);
		departmentList1.add(department102);

		Company company = new Company(111, "Mircosoft中国", new Date(), departmentList1);
		
		Company result = companyMgr.add(company);
		return buildSuccess("company", result);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject delete(@RequestParam(value = "id") Integer id) throws Exception {
		companyMgr.delete(id);
		return buildSuccess(null, null);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public JsonObject update() throws Exception {
		Department department101 = new Department(111, "Phone_修改");
		Department department102 = new Department(222, "Visio Studio_修改");
		List<Department> departmentList1 = new ArrayList<Department>();
		departmentList1.add(department101);
		departmentList1.add(department102);

		Company company = new Company(111, "Mircosoft中国_修改", new Date(), departmentList1);
		
		companyMgr.update(company);
		return buildSuccess(null, null);
	}
	
	
}
