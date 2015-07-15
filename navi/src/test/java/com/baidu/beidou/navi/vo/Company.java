package com.baidu.beidou.navi.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ClassName: Company <br/>
 * Function: 公司
 * 
 * @author Zhang Xu
 */
public class Company extends BaseObject {

    private String name;

    private Date establishTime;

    private List<Department> departmentList;

    public Company() {
        super();
    }

    public Company(int id, String name, Date establishTime, List<Department> departmentList) {
        super();
        this.setId(id);
        this.name = name;
        this.establishTime = establishTime;
        this.departmentList = departmentList;
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
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.getId())
                .append("name", name).append("establishTime", establishTime)
                .append("departmentList", departmentList).toString();
    }

}
