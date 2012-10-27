package com.stkdevelopers.easytransfer.tools;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import java.util.Date;



public class Tarjeta {
	
	private String id;
	private String number;
	private String holder;
	private String linkAccount;
	private Date deprecateDate;
	private String securityCode;
	private String mode;
	private String issuer;
	private double creditLimit;
	private Date dueDate;
	private double totalDebt;
	private double creditAvailable;
	private double interestRate;
	
	
	public Tarjeta(String id, String number, String holder, String linkAccount,
			Date deprecateDate, String securityCode, String mode, String issuer,
			double creditLimit, Date dueDate, double totalDebt,
			double creditAvailable, double interestRate) {
		super();
		this.id = id;
		this.number = number;
		this.holder = holder;
		this.linkAccount = linkAccount;
		this.deprecateDate = deprecateDate;
		this.securityCode = securityCode;
		this.mode = mode;
		this.issuer = issuer;
		this.creditLimit = creditLimit;
		this.dueDate = dueDate;
		this.totalDebt = totalDebt;
		this.creditAvailable = creditAvailable;
		this.interestRate = interestRate;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getHolder() {
		return holder;
	}


	public void setHolder(String holder) {
		this.holder = holder;
	}


	public String getLinkAccount() {
		return linkAccount;
	}


	public void setLinkAccount(String linkAccount) {
		this.linkAccount = linkAccount;
	}


	public Date getDeprecateDate() {
		return deprecateDate;
	}


	public void setDeprecateDate(Date deprecateDate) {
		this.deprecateDate = deprecateDate;
	}


	public String getSecurityCode() {
		return securityCode;
	}


	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getIssuer() {
		return issuer;
	}


	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}


	public double getCreditLimit() {
		return creditLimit;
	}


	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}


	public double getTotalDebt() {
		return totalDebt;
	}


	public void setTotalDebt(double totalDebt) {
		this.totalDebt = totalDebt;
	}


	public double getCreditAvailable() {
		return creditAvailable;
	}


	public void setCreditAvailable(double creditAvailable) {
		this.creditAvailable = creditAvailable;
	}


	public double getInterestRate() {
		return interestRate;
	}


	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	
	

}
