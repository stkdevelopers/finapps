package com.stkdevelopers.easytransfer.tools;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import java.util.ArrayList;

import android.location.Address;
/**
 *   data :
 *   id : STRING, 
 *   holder: { username: STRING, password, STRING, firstName: STRING, lastName: STRING, 
 *   address: { street: STRING, number: STRING, city: STRING, postalCode: STRING, county: STRING } }, 
 *   accounts:[id_account,...], 
 *   cards:[id_card,...] } }
 * */
public class Operation {
	
	//attrs
	private String id;
	private String account;
	private String date;
	private String type; //enum[DEPOSIT|CHARGE|PAYROLL|TRANSFER]
	private String concept;
	private String description;
	private String value;
	private String actualBalance;
	private String options;
	
	

	public Operation() {
		this.id="0";
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getActualBalance() {
		return actualBalance;
	}
	public void setActualBalance(String actualBalance) {
		this.actualBalance = actualBalance;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
	
	
	

}
