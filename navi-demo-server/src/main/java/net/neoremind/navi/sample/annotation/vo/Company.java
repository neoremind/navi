package net.neoremind.navi.sample.annotation.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Company {

	private Integer id;

	private String name;

	private Date establishTime;

	private List<Department> departmentList;

	public Company(Integer id, String name, Date establishTime, List<Department> departmentList) {
		super();
		this.id = id;
		this.name = name;
		this.establishTime = establishTime;
		this.departmentList = departmentList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
		.append("id", id)
		.append("name", name)
		.append("establishTime", establishTime)
		.append("departmentList", departmentList)
		.toString();
	}
	
}
