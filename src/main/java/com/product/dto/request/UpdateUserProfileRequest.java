package com.product.dto.request;

public class UpdateUserProfileRequest {

	private String firstName;
	private String lastName;
	private String zipCode;
	private String city;
	private String countryCode;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
