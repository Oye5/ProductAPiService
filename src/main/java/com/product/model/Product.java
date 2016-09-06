package com.product.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	@Size(max = 45)
	private String displayName;

	@Column(name = "category_id")
	private int categoryId;

	@Column(name = "language_code")
	@Size(max = 45)
	private String languageCode;

	@Column(name = "price")
	private int price;

	@Column(name = "currency")
	@Size(max = 45)
	private String currency;

	@Column(name = "status")
	@Size(max = 45)
	private String status;

	@Column(name = "conditions")
	@Size(max = 45)
	private String condition;

	@ManyToOne(cascade = CascadeType.DETACH)
    //@ManyToOne
	@JoinColumn(name = "geo_id")
	private Geo geo;

	@ManyToOne(cascade = CascadeType.DETACH)
    //@ManyToOne
	@JoinColumn(name = "seller_id")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

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

	public int getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getStatus() {
		return status;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
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

	public void setPrice(int price) {
		this.price = price;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
