package com.baidu.beidou.sample.performancetest.service.vo;

import java.util.Date;

public class BankAccount {

	private Integer bankId;

	private String bankName;

	private int balance;

	private Date lastedWithdrawTime;

	public BankAccount() {
		super();
	}

	public BankAccount(Integer bankId, String bankName, int balance, Date lastedWithdrawTime) {
		super();
		this.bankId = bankId;
		this.bankName = bankName;
		this.balance = balance;
		this.lastedWithdrawTime = lastedWithdrawTime;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Date getLastedWithdrawTime() {
		return lastedWithdrawTime;
	}

	public void setLastedWithdrawTime(Date lastedWithdrawTime) {
		this.lastedWithdrawTime = lastedWithdrawTime;
	}

}
