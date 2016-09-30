package com.product.dto.response;

public class UserResponse {
	private String user_id;
	private String user_name;
	private String first_name;
	private String last_name;
	private String email;
	private String profile_pic_url;
	private String country_code;
	private String city;
	private String zip_code;
	private String banned;
	private String status;

	public String getUser_id() {
		return user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getEmail() {
		return email;
	}

	public String getProfile_pic_url() {
		return profile_pic_url;
	}

	public String getCountry_code() {
		return country_code;
	}

	public String getCity() {
		return city;
	}

	public String getZip_code() {
		return zip_code;
	}

	public String getBanned() {
		return banned;
	}

	public String getStatus() {
		return status;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setProfile_pic_url(String profile_pic_url) {
		this.profile_pic_url = profile_pic_url;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public void setBanned(String banned) {
		this.banned = banned;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
