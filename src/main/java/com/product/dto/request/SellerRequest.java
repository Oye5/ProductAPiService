package com.product.dto.request;

public class SellerRequest {

	private String id;
	private String name;
	private String profile_pic_url;
	private String country_code;
	private String city;
	private String zip_code;
	private String status;
	private String banned;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public String getStatus() {
		return status;
	}

	public String getBanned() {
		return banned;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setStatus(String status) {
		this.status = status;
	}

	public void setBanned(String banned) {
		this.banned = banned;
	}

}
