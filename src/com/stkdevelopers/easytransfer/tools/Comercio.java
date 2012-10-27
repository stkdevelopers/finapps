package com.stkdevelopers.easytransfer.tools;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import android.location.Address;
import android.location.Location;

/*
 * 
 * data : 
 * { 
 * publicName: STRING 
 * address: { street: STRING, number: STRING, city: STRING, postalCode: STRING, county: STRING }, 
 * location:[lat<DOUBLE>, long<DOUBLE>]] }
 * **/

public class Comercio {

	private String id;
	private String name;
	private double latitude;
	private double longitude;
	private Location location;
	private String street;
	private String number;
	private String city;
	private String postalCode;
	private String country;
	


	
	public Comercio(String id, String name, double latitude, double longitude,
			String street, String number, String city,
			String postalCode, String country) {
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.street = street;
		this.number = number;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;		
		this.location = new Location("GPS");
		this.location.setLatitude(latitude);
		this.location.setLongitude(longitude);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongutyude() {
		return longitude;
	}

	public void setLongutyude(double longutyude) {
		this.longitude = longutyude;
	}

	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	
	
}
