package net.neoremind.navi.server.service;

import java.util.List;

import net.neoremind.navi.server.Company;

public interface CompanyService {

	List<Company> getAll();
	
	Company getById(int id);
	
	Company add(Company company);
	
}
