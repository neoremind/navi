package net.neoremind.navi.server.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.server.Company;
import net.neoremind.navi.server.service.CompanyService;

public class CompanyServiceImpl implements CompanyService {

	private final static Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class); 

	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static List<String> departmentNameList = new ArrayList<String>();
	
	static {
		departmentNameList.add("R&D");
		departmentNameList.add("Q&A");
		departmentNameList.add("财务");
	}
	
	public List<Company> getAll() {
		List<Company> result = new ArrayList<Company>();
		try {
			Company company1 = new Company(1000, "脸书-Facebook", dateFormat.parse("1999-06-01"), "Mark", departmentNameList);
			Company company2 = new Company(2000, "百度", dateFormat.parse("2013-06-01"), "Robin Li", null);
			result.add(company1);
			result.add(company2);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Company getById(int id) {
		try {
			if (id == 1000) {
				Company company = new Company(1000, "脸书-Facebook", dateFormat.parse("1999-06-01"), "Mark", departmentNameList);
				return company;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public Company add(Company company) {
		return company;
	}

}
