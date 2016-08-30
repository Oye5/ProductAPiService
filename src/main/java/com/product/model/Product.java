package com.product.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

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
	private String product_id;

	@Column(name = "description")
	@Size(max = 2048)
	private String description;

	@Column(name = "display_name")
	@Size(max = 45)
	private String display_name;

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

	@ManyToOne
	@JoinColumn(name = "geo_id")
	private Geo geo;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date created_at;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updated_at;

	public String getProduct_id() {
		return product_id;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplay_name() {
		return display_name;
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

	public Date getCreated_at() {
		return created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
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

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Geo getGeo() {
		return geo;
	}

	public User getUserId() {
		return userId;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

}
