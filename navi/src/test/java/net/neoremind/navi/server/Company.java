package net.neoremind.navi.server;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Company implements Serializable {

	private static final long serialVersionUID = -3093569619188513043L;

	private int id;

	private String name;

	private Date buildTime;

	private String ceo;

	private List<String> departmentNameList;

	public Company() {

	}

	public Company(int id, String name, Date buildTime, String ceo, List<String> departmentNameList) {
		super();
		this.id = id;
		this.name = name;
		this.buildTime = buildTime;
		this.ceo = ceo;
		this.departmentNameList = departmentNameList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDepartmentNameList() {
		return departmentNameList;
	}

	public void setDepartmentNameList(List<String> departmentNameList) {
		this.departmentNameList = departmentNameList;
	}

}
