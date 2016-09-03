package com.product.dto.request;

public class ProductRequest {
	private String productId;
	private String description;
	private String displayName;
	private int categoryId;
	private String languageCode;
	private int price;
	private String currency;
	private String status;
	private String condition;
	private int geoId;
	private String sellerId;
	private String createdAt;
	private String updatedAt;
	private GeoRequest geo;
	private SellerRequest seller;
	private ImageRequest images;

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

	public int getGeoId() {
		return geoId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
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

	public void setGeoId(int geoId) {
		this.geoId = geoId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
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

}
