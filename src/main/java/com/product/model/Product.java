package com.product.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.elasticsearch.common.geo.GeoPoint;

/***
 *
 */

@Entity
@Table(name = "product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3366441338924521575L;

	@Id
	@Column(name = "product_id")
	@Size(max = 50)
	private String productId;

	@Column(name = "description")
	@Size(max = 2048)
	private String description;

	@Column(name = "display_name")
	@Size(max = 2045)
	private String displayName;

	@Column(name = "category_id")
	private int categoryId;

	@Column(name = "language_code")
	@Size(max = 45)
	private String languageCode;

	@Column(name = "price")
	private double price;

	@Column(name = "currency")
	@Size(max = 45)
	private String currency;

	@Column(name = "status", length = 30)
	private String status;

	@Column(name = "conditions")
	@Size(max = 45)
	private String condition;

	@ManyToOne(cascade = CascadeType.DETACH)
	// @ManyToOne
	@JoinColumn(name = "geo_id")
	private Geo geo;

	@ManyToOne(cascade = CascadeType.DETACH)
	// @ManyToOne
	@JoinColumn(name = "seller_id")
	private User user;

	@Column(name = "created_at")
	private String createdAt;

	@Column(name = "updated_at")
	private String updatedAt;

	@Column(name = "country")
	private String country;

	@Column(name = "address")
	private String address;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "state")
	private String state;

	@Column(name = "warranty")
	private String warranty;

	@Column(name = "brand")
	private String brand;

	@Column(name = "quantity")
	private int quantity;

	@Transient
	private GeoPoint location;

	public String getProductId() {
		return productId;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getCurrency() {
		return currency;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCondition() {
		return condition;
	}

	public Geo getGeo() {
		return geo;
	}

	public User getUser() {
		return user;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public String getAddress() {
		return address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getState() {
		return state;
	}

	public String getWarranty() {
		return warranty;
	}

	public String getBrand() {
		return brand;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
