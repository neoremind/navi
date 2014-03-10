package net.neoremind.navi.sample.annotation.service;

import java.util.List;

import net.neoremind.navi.sample.annotation.vo.Company;

public interface CompanyMgr {

	/**
	 * query
	 */
	public Company get(int id);

	public List<Company> getAll();

	/**
	 * add
	 */
	public Company add(Company company);

	/**
	 * delete
	 */
	public void delete(int id);

	/**
	 * update
	 */
	public void update(Company company);
	
}
