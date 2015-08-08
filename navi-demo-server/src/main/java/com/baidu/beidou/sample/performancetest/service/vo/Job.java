package com.baidu.beidou.sample.performancetest.service.vo;


public class Job {

	private String companyName;

	private String title;

	private Long salary;

	
	
	public Job() {
		super();
	}

	public Job(String companyName, String title, Long salary) {
		super();
		this.companyName = companyName;
		this.title = title;
		this.salary = salary;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

}
