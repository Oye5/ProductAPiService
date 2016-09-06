package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "geo")
public class Geo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371441538985136993L;

	@Id
	@Column(name = "geo_id")
	private String geo_id;

	@Column(name = "lat")
	private double lattitude;

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "city")
	private String city;

	@Column(name = "zip_code")
	private String zipCode;

	public String getGeo_id() {
		return geo_id;
	}

	public double getLattitude() {
		return lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setGeo_id(String geo_id) {
		this.geo_id = geo_id;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
