package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "seller_profile")
public class Seller implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	// @Column(name = "user_id")
	// @Size(max = 45)
	// private String userId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@Column(name = "first_name")
	@Size(max = 255)
	private String firstName;

	@Column(name = "last_name")
	@Size(max = 255)
	private String lastName;

	@Column(name = "zip_code")
	@Size(max = 6)
	private String zipCode;

	@Column(name = "city")
	@Size(max = 255)
	private String city;

	@Column(name = "country_code")
	@Size(max = 10)
	private String countryCode;

	@Column(name = "profile_pic")
	@Size(max = 255)
	private String profilePic;

	@Column(name = "active")
	private int active;

	@Column(name = "banned")
	private String banned;

	@Column(name = "status")
	private String status;

	public User getUserId() {
		return userId;
	}

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

	public String getProfilePic() {
		return profilePic;
	}

	public int getActive() {
		return active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserId(User userId) {
		this.userId = userId;
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

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getBanned() {
		return banned;
	}

	public String getStatus() {
		return status;
	}

	public void setBanned(String banned) {
		this.banned = banned;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
