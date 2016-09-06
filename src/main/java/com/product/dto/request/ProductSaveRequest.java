package com.product.dto.request;

import java.util.ArrayList;
import java.util.List;

public class ProductSaveRequest {
	private String description;
	private String displayName;
	private int categoryId;
	private String languageCode;
	private int price;
	private String currency;
	private String status;
	private String condition;
	private String sellerId;
	private String createdAt;
	private String updatedAt;
	private double lattitude;
	private double longitude;
	private List<String> imageList = new ArrayList<String>();

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

	public String getSellerId() {
		return sellerId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public double getLattitude() {
		return lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public List<String> getImageList() {
		return imageList;
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

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

}
