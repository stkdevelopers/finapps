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


public class UserProfile {
		
	public static final int CLIENT =0;
	public static final int COMMERCE =1;
	//attrs
	private String id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String address;
	private ArrayList<Account> accounts;
	private ArrayList<Tarjeta> tarjetas;
	private int profileRole;
	private String publicProfile;
	
	
	//constructor
	public UserProfile(String id,int profileRole, String username, String password, String firstname,
			String lastname, String address, ArrayList<Account> accounts,
			ArrayList<Tarjeta> tarjetas, String publicProfile) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.accounts = accounts;
		this.tarjetas = tarjetas;
		this.setProfileRole(profileRole);
		this.publicProfile = publicProfile;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	public ArrayList<Tarjeta> getTarjetas() {
		return tarjetas;
	}
	public void setTarjetas(ArrayList<Tarjeta> tarjetas) {
		this.tarjetas = tarjetas;
	}


	public int getProfileRole() {
		return profileRole;
	}


	public void setProfileRole(int profileRole) {
		this.profileRole = profileRole;
	}
	
	

}
