package com.baidu.beidou.sample.performancetest.service.vo;

import java.util.Date;
import java.util.List;


public class Person {

	private int id;

	private byte gender;
	
	private String name;
	
	private String address;
	
	private String phoneNumber;
	
	private Date birthday;

	private Job job;
	
	private List<BankAccount> accountList;
	
	public Person() {
		super();
	}

	public Person(int id, byte gender, String name, String address, String phoneNumber, Date birthday, Job job, List<BankAccount> accountList) {
		super();
		this.id = id;
		this.gender = gender;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.job = job;
		this.accountList = accountList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public List<BankAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<BankAccount> accountList) {
		this.accountList = accountList;
	}
	
}
