package com.baidu.beidou.sample.annotation.service;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.sample.annotation.vo.Company;

public interface CompanyMgr {

	/**
	 * query
	 */
	Company get(int id);
	
	List<Company> getByIds(List<Integer> ids);
	
	Map<Integer, Company> getMapByIds(List<Integer> ids);

	List<Company> getAll();

	/**
	 * add
	 */
	Company add(Company company);

	/**
	 * delete
	 */
	void delete(int id);

	/**
	 * update
	 */
	void update(Company company);
	
}
