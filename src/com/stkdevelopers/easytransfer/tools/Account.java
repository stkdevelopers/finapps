package com.stkdevelopers.easytransfer.tools;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/

public class Account {

	public static int CURRENCY_EURO = 0;
	public static int CURRENCY_DOLLAR = 1;

	
	private String id;
	private String accountNumber;
	private String IBAN;
	private int currency;
	private double availableBalance;
	private double retainedBalance;
	private double actualBalance;
	
	public Account(String id, String accountNumber, String iBAN, int currency,
			double availableBalance, double retainedBalance,
			double actualBalance) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		IBAN = iBAN;
		this.currency = currency;
		this.availableBalance = availableBalance;
		this.retainedBalance = retainedBalance;
		this.actualBalance = actualBalance;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIBAN() {
		return IBAN;
	}
	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public double getRetainedBalance() {
		return retainedBalance;
	}
	public void setRetainedBalance(double retainedBalance) {
		this.retainedBalance = retainedBalance;
	}
	public double getActualBalance() {
		return actualBalance;
	}
	public void setActualBalance(double actualBalance) {
		this.actualBalance = actualBalance;
	}
	
	
	
	
}
