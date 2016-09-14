package com.product.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product_images")
public class ProductImages implements Serializable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "url", length = 255)
	private String url;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Product getProductId() {
		return productId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

}
