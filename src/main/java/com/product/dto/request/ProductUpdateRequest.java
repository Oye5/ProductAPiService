package com.product.dto.request;

public class ProductUpdateRequest {

	private String description;
	private String display_name;
	private int quantity;
	private int category_id;
	private String image;
	private String language_code;
	private double price;
	private String currency;
	private String status;
	private String condition;

	private GeoRequest geo;
	private SellerRequest seller;
	private ImageRequest images;
	private String createdAt;
	private String updatedAt;

	public String getDescription() {
		return description;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getCategory_id() {
		return category_id;
	}

	public String getImage() {
		return image;
	}

	public String getLanguage_code() {
		return language_code;
	}

	public double getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public String getCondition() {
		return condition;
	}

	public GeoRequest getGeo() {
		return geo;
	}

	public SellerRequest getSeller() {
		return seller;
	}

	public ImageRequest getImages() {
		return images;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public void setGeo(GeoRequest geo) {
		this.geo = geo;
	}

	public void setSeller(SellerRequest seller) {
		this.seller = seller;
	}

	public void setImages(ImageRequest images) {
		this.images = images;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
