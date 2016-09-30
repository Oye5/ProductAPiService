package com.product.dto.response;

public class GeoResponse {

	private double lat;
	private double lng;
	private String country_code;
	private String city;
	private String zip_code;
	private double distance;

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
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

	public double getDistance() {
		return distance;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
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

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
