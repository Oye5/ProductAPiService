package com.product.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "product_images_thumb")
public class ThumbNail implements Serializable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "url", length = 255)
	private String url;

	@Column(name = "width")
	private double width;

	@Column(name = "height")
	private double height;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	@JsonIgnore
	@JsonProperty(value = "productId")
	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

}
