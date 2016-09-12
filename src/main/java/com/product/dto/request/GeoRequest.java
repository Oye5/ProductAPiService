package com.product.dto.request;

public class GeoRequest {
	private double lat;
	private double longitude;
	private String country_code;
	private String city;
	private String zip_code;

	public double getLat() {
		return lat;
	}

	public double getLongitude() {
		return longitude;
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

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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

}
